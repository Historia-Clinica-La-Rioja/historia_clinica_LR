package net.pladema.medicalconsultation.diary.service.impl;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.UpdateAppointmentOpeningHoursService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OverturnsLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;
import static ar.lamansys.sgx.shared.dates.utils.DateUtils.isBetween;
import static ar.lamansys.sgx.shared.security.UserInfo.getCurrentAuditor;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

	private static final String INPUT_DIARY_ID = "Input parameters -> diaryId {}";
	private static final Logger LOG = LoggerFactory.getLogger(DiaryServiceImpl.class);
	private static final String OUTPUT = "Output -> {}";

	private final DiaryOpeningHoursService diaryOpeningHoursService;

	private final AppointmentService appointmentService;

	private final UpdateAppointmentOpeningHoursService updateApmtOHService;

	private final DiaryRepository diaryRepository;

	@Override
	public Integer addDiary(DiaryBo diaryToSave) {
		LOG.debug("Input parameters -> diaryToSave {}", diaryToSave);

		Diary diary = createDiaryInstance(diaryToSave);
		Integer diaryId = persistDiary(diaryToSave, diary);

		diaryToSave.setId(diaryId);
		LOG.debug("Diary saved -> {}", diaryToSave);
		return diaryId;
	}

	private Integer persistDiary(DiaryBo diaryToSave, Diary diary) {
		diary = diaryRepository.save(diary);
		Integer diaryId = diary.getId();
		diaryOpeningHoursService.update(diaryId, diaryToSave.getDiaryOpeningHours());
		return diaryId;
	}

	private Diary createDiaryInstance(DiaryBo diaryBo) {
		Diary diary = new Diary();
		return mapDiaryBo(diaryBo, diary);
	}

	private Diary mapDiaryBo(DiaryBo diaryBo, Diary diary) {
		diary.setId(diaryBo.getId() != null ? diaryBo.getId() : null);
		diary.setHealthcareProfessionalId(diaryBo.getHealthcareProfessionalId());
		diary.setDoctorsOfficeId(diaryBo.getDoctorsOfficeId());
		diary.setStartDate(diaryBo.getStartDate());
		diary.setEndDate(diaryBo.getEndDate());
		diary.setAppointmentDuration(diaryBo.getAppointmentDuration());
		diary.setAutomaticRenewal(diaryBo.isAutomaticRenewal());
		diary.setProfessionalAsignShift(diaryBo.isProfessionalAssignShift());
		diary.setIncludeHoliday(diaryBo.isIncludeHoliday());
		diary.setActive(true);
		return diary;
	}

	@Override
	public Boolean deleteDiary(Integer diaryId) {
		LOG.debug(INPUT_DIARY_ID, diaryId);
		Diary diaryToDelete = diaryRepository.findById(diaryId)
				.orElseThrow(() -> new EntityNotFoundException("diary.invalid.id"));
		diaryToDelete.setDeleted(Boolean.TRUE);
		diaryToDelete.setDeletedBy(getCurrentAuditor());
		diaryToDelete.setDeletedOn(LocalDateTime.now());
		diaryToDelete.setActive(false);
		diaryRepository.save(diaryToDelete);
		LOG.debug("Diary with diaryId {} DELETED", diaryId);
		return Boolean.TRUE;
	}

	@Override
	public Integer updateDiary(DiaryBo diaryToUpdate) {
		LOG.debug("Input parameters -> diaryToUpdate {}", diaryToUpdate);
		Diary savedDiary = diaryRepository.getOne(diaryToUpdate.getId());
		HashMap<DiaryOpeningHoursBo, List<AppointmentBo>> apmtsByNewDOH = new HashMap<>();
		diaryToUpdate.getDiaryOpeningHours().stream().forEach(doh -> apmtsByNewDOH.put(doh, new ArrayList<>()));
		Collection<AppointmentBo> apmts = appointmentService.getFutureActiveAppointmentsByDiary(diaryToUpdate.getId());
		adjustExistingAppointmentsOpeningHours(apmtsByNewDOH, apmts);
		persistDiary(diaryToUpdate, mapDiaryBo(diaryToUpdate, savedDiary));
		updatedExistingAppointments(diaryToUpdate, apmtsByNewDOH);
		LOG.debug("Diary updated -> {}", diaryToUpdate);
		return diaryToUpdate.getId();
	}

	private void adjustExistingAppointmentsOpeningHours(HashMap<DiaryOpeningHoursBo, List<AppointmentBo>> apmtsByNewDOH,
			Collection<AppointmentBo> apmts) {
		apmtsByNewDOH.forEach((doh, apmtsList) -> {
			apmtsList.addAll(apmts.stream().filter(apmt -> belong(apmt, doh)).collect(toList()));
			if (overturnsOutOfLimit(doh, apmtsList)) {
				throw new OverturnsLimitException();
			}
		});
	}

	private void updatedExistingAppointments(DiaryBo diaryToUpdate,
			HashMap<DiaryOpeningHoursBo, List<AppointmentBo>> apmtsByNewDOH) {
		Collection<DiaryOpeningHoursBo> dohSavedList = diaryOpeningHoursService
				.getDiariesOpeningHours(Stream.of(diaryToUpdate.getId()).collect(toList()));
		apmtsByNewDOH.forEach((doh, apmts) -> dohSavedList.stream().filter(doh::equals).findAny().ifPresent(
				savedDoh -> apmts.forEach(apmt -> apmt.setOpeningHoursId(savedDoh.getOpeningHours().getId()))));
		List<AppointmentBo> apmtsToUpdate = apmtsByNewDOH.values().stream().flatMap(Collection::stream)
				.collect(toList());
		apmtsToUpdate.stream().forEach(updateApmtOHService::execute);

	}

	private boolean overturnsOutOfLimit(DiaryOpeningHoursBo doh, List<AppointmentBo> apmtsList) {
		Map<LocalDate, Long> overturnsByDate = apmtsList.stream().filter(AppointmentBo::isOverturn)
				.collect(groupingBy(AppointmentBo::getDate, counting()));
		return overturnsByDate.values().stream().anyMatch(overturns -> overturns > doh.getOverturnCount().intValue());
	}

	private boolean belong(AppointmentBo apmt, DiaryOpeningHoursBo doh) {
		return getWeekDay(apmt.getDate()).equals(doh.getOpeningHours().getDayWeekId())
				&& isBetween(apmt.getHour(), doh.getOpeningHours().getFrom(), doh.getOpeningHours().getTo());
	}

	/**
	 *
	 * @param healthcareProfessionalId ID profesional de salud
	 * @param doctorsOfficeId          ID consultorio
	 * @param newDiaryStart            nueva fecha de comienzo para agenda
	 * @param newDiaryEnd              nueva fecha de fin para agenda
	 * @return lista con todos los ID de agendas definidas en rangos de fecha
	 *         superpuestas para un mismo profesional de salud y consultorio.
	 */
	@Override
	public List<Integer> getAllOverlappingDiaryByProfessional(Integer healthcareProfessionalId, Integer doctorsOfficeId,
															  LocalDate newDiaryStart, LocalDate newDiaryEnd, Optional<Integer> excludeDiaryId) {
		LOG.debug(
				"Input parameters -> healthcareProfessionalId {}, doctorsOfficeId {}, newDiaryStart {}, newDiaryEnd {}",
				healthcareProfessionalId, doctorsOfficeId, newDiaryStart, newDiaryEnd);
		List<Integer> diaryIds = excludeDiaryId.isPresent()
				? diaryRepository.findAllOverlappingDiaryByProfessionalExcludingDiary(healthcareProfessionalId, doctorsOfficeId,
						newDiaryStart, newDiaryEnd, excludeDiaryId.get())
				: diaryRepository.findAllOverlappingDiaryByProfessional(healthcareProfessionalId, doctorsOfficeId, newDiaryStart,
						newDiaryEnd);
		LOG.debug("Diary saved -> {}", diaryIds);
		return diaryIds;

	}

	@Override
	public List<DiaryBo> getAllOverlappingDiary(@NotNull Integer doctorsOfficeId,
												@NotNull LocalDate newDiaryStart, @NotNull  LocalDate newDiaryEnd, Optional<Integer> excludeDiaryId) {
		LOG.debug(
				"Input parameters -> doctorsOfficeId {}, newDiaryStart {}, newDiaryEnd {}",
				doctorsOfficeId, newDiaryStart, newDiaryEnd);
		List<Diary> diaries = excludeDiaryId.isPresent()
				? diaryRepository.findAllOverlappingDiaryExcludingDiary(doctorsOfficeId,
				newDiaryStart, newDiaryEnd, excludeDiaryId.get())
				: diaryRepository.findAllOverlappingDiary(doctorsOfficeId, newDiaryStart,
				newDiaryEnd);
		List<DiaryBo> result = diaries.stream().map(this::createDiaryBoInstance).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Collection<DiaryBo> getActiveDiariesBy(Integer healthcareProfessionalId, Integer specialtyId, Integer institutionId) {
		LOG.debug("Input parameters -> healthcareProfessionalId {}, specialtyId {}, institutionId {}", healthcareProfessionalId, specialtyId, institutionId);
		List<DiaryListVo> diaries;

		if (specialtyId == null)
			diaries = diaryRepository.getActiveDiariesFromProfessional(healthcareProfessionalId, institutionId);
		else
			diaries = diaryRepository.getActiveDiariesFromProfessionalAndSpecialty(healthcareProfessionalId, specialtyId, institutionId);

		List<DiaryBo> result = diaries.stream().map(this::createDiaryBoInstance).collect(toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	private DiaryBo createDiaryBoInstance(DiaryListVo diaryListVo) {
		LOG.debug("Input parameters -> diaryListVo {}", diaryListVo);
		DiaryBo result	 = new DiaryBo();
		result.setId(diaryListVo.getId());
		result.setDoctorsOfficeId(diaryListVo.getDoctorsOfficeId());
		result.setDoctorsOfficeDescription(diaryListVo.getDoctorsOfficeDescription());
		result.setStartDate(diaryListVo.getStartDate());
		result.setEndDate(diaryListVo.getEndDate());
		result.setAppointmentDuration(diaryListVo.getAppointmentDuration());
		result.setAutomaticRenewal(diaryListVo.isAutomaticRenewal());
		result.setProfessionalAssignShift(diaryListVo.isProfessionalAssignShift());
		result.setIncludeHoliday(diaryListVo.isIncludeHoliday());
		LOG.debug(OUTPUT, result);
		return result;
	}

	private CompleteDiaryBo createCompleteDiaryBoInstance(CompleteDiaryListVo completeDiaryListVo) {
		LOG.debug("Input parameters -> diaryListVo {}", completeDiaryListVo);
		CompleteDiaryBo result = new CompleteDiaryBo(createDiaryBoInstance(completeDiaryListVo));
		result.setSectorId(completeDiaryListVo.getSectorId());
		result.setClinicalSpecialtyId(completeDiaryListVo.getClinicalSpecialtyId());
		result.setHealthcareProfessionalId(completeDiaryListVo.getHealthcareProfessionalId());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<CompleteDiaryBo> getDiary(Integer diaryId) {
		LOG.debug(INPUT_DIARY_ID, diaryId);
		Optional<CompleteDiaryBo> result = diaryRepository.getDiary(diaryId).map(this::createCompleteDiaryBoInstance)
				.map(completeOpeningHours());
		LOG.debug(OUTPUT, result);
		return result;
	}

	private Function<CompleteDiaryBo, CompleteDiaryBo> completeOpeningHours() {
		return completeDiary -> {
			Collection<DiaryOpeningHoursBo> diaryOpeningHours = diaryOpeningHoursService
					.getDiariesOpeningHours(Stream.of(completeDiary.getId()).collect(toList()));
			completeDiary.setDiaryOpeningHours(diaryOpeningHours.stream().collect(toList()));
			return completeDiary;
		};
	}

	@Override
	public DiaryBo getDiaryById(Integer diaryId) {
		LOG.debug(INPUT_DIARY_ID, diaryId);
		Diary resultQuery = diaryRepository.findById(diaryId)
				.orElseThrow(() -> new NotFoundException("diaryId", "diaryId -> " + diaryId + " does not exist"));
		DiaryBo result = createDiaryBoInstance(resultQuery);
		LOG.debug(OUTPUT, result);
		return result;
	}

	private DiaryBo createDiaryBoInstance(Diary diary) {
		LOG.debug("Input parameters -> diary {}", diary);
		DiaryBo result = new DiaryBo();
		result.setId(diary.getId());
		result.setDoctorsOfficeId(diary.getDoctorsOfficeId());
		result.setStartDate(diary.getStartDate());
		result.setEndDate(diary.getEndDate());
		result.setAppointmentDuration(diary.getAppointmentDuration());
		result.setAutomaticRenewal(diary.isAutomaticRenewal());
		result.setProfessionalAssignShift(diary.isProfessionalAsignShift());
		result.setIncludeHoliday(diary.isIncludeHoliday());
		result.setHealthcareProfessionalId(diary.getHealthcareProfessionalId());
		result.setDeleted(diary.isDeleted());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<DiaryBo> getDiaryByAppointment(Integer appointmentId) {
		LOG.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<DiaryBo> result = diaryRepository.getDiaryByAppointment(appointmentId).map(this::createDiaryBoInstance);
		LOG.debug(OUTPUT, result);
		return result;
	}

}
