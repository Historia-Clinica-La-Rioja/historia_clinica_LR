package net.pladema.medicalconsultation.diary.service.impl;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.UpdateAppointmentOpeningHoursService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.appointment.service.domain.EmptyAppointmentBo;
import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.diary.service.DiaryAssociatedProfessionalService;
import net.pladema.medicalconsultation.diary.service.DiaryCareLineService;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryPracticeService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OverturnsLimitException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryException;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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

	private final DiaryAssociatedProfessionalService diaryAssociatedProfessionalService;

	private final DiaryCareLineService diaryCareLineService;

	private final DiaryRepository diaryRepository;

	private final LoggedUserExternalService loggedUserExternalService;

	private final InstitutionExternalService institutionExternalService;

	private final DateTimeProvider dateTimeProvider;

	private final DiaryPracticeService diaryPracticeService;

	@Override
	@Transactional
	public Integer addDiary(DiaryBo diaryToSave) throws DiaryException {
		LOG.debug("Input parameters -> diaryToSave {}", diaryToSave);

		validateDiary(diaryToSave);

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
		diaryCareLineService.updateCareLinesAssociatedToDiary(diaryId, diaryToSave.getCareLines());
		diaryAssociatedProfessionalService.updateDiaryAssociatedProfessionals(diaryToSave.getDiaryAssociatedProfessionalsId(), diaryId);
		diaryPracticeService.updateDiaryPractices(diaryToSave.getPracticesId(), diaryId);
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
		diary.setClinicalSpecialtyId(diaryBo.getClinicalSpecialtyId());
		diary.setAlias(diaryBo.getAlias());
		diary.setPredecessorProfessionalId(diaryBo.getPredecessorProfessionalId());
		diary.setHierarchicalUnitId(diaryBo.getHierarchicalUnitId());
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
	public Integer updateDiary(DiaryBo diaryToUpdate) throws DiaryException {
		LOG.debug("Input parameters -> diaryToUpdate {}", diaryToUpdate);

		validateDiary(diaryToUpdate);

		return diaryRepository.findById(diaryToUpdate.getId()).map(savedDiary -> {
			HashMap<DiaryOpeningHoursBo, List<AppointmentBo>> apmtsByNewDOH = new HashMap<>();
			diaryToUpdate.getDiaryOpeningHours().forEach( doh -> {
				if (doh.getProtectedAppointmentsAllowed() != null && doh.getProtectedAppointmentsAllowed() && diaryToUpdate.getCareLines().isEmpty())
					doh.setProtectedAppointmentsAllowed(false);
				doh.setDiaryId(savedDiary.getId());
				apmtsByNewDOH.put(doh, new ArrayList<>());
			});
			Collection<AppointmentBo> apmts = appointmentService.getAppointmentsByDiaries(List.of(diaryToUpdate.getId()), diaryToUpdate.getStartDate(), diaryToUpdate.getEndDate());
			adjustExistingAppointmentsOpeningHours(apmtsByNewDOH, apmts);
			persistDiary(diaryToUpdate, mapDiaryBo(diaryToUpdate, savedDiary));
			updatedExistingAppointments(diaryToUpdate, apmtsByNewDOH);
			LOG.debug("Diary updated -> {}", diaryToUpdate);
			return diaryToUpdate.getId();
		}).get();

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
		apmtsToUpdate.forEach(appointment -> updateApmtOHService.execute(appointment, false));
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

	@Override
	public List<DiaryBo> getAllOverlappingDiary(@NotNull Integer healthcareProfessionalId, @NotNull Integer doctorsOfficeId,
												@NotNull LocalDate newDiaryStart, @NotNull  LocalDate newDiaryEnd, Optional<Integer> excludeDiaryId) {
		LOG.debug(
				"Input parameters -> doctorsOfficeId {}, newDiaryStart {}, newDiaryEnd {}",
				doctorsOfficeId, newDiaryStart, newDiaryEnd);
		List<Diary> diaries = excludeDiaryId.isPresent()
				? diaryRepository.findAllOverlappingDiaryExcludingDiary(healthcareProfessionalId, doctorsOfficeId,
				newDiaryStart, newDiaryEnd, excludeDiaryId.get())
				: diaryRepository.findAllOverlappingDiary(healthcareProfessionalId, doctorsOfficeId, newDiaryStart,
				newDiaryEnd);
		List<DiaryBo> result = diaries.stream().map(this::createDiaryBoInstance).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Collection<DiaryBo> getActiveDiariesBy(Integer associatedHealthcareProfessionalId, Integer healthcareProfessionalId, Integer specialtyId, Integer institutionId) {
		LOG.debug("Input parameters -> healthcareProfessionalId {}, specialtyId {}, institutionId {}", healthcareProfessionalId, specialtyId, institutionId);
		List<DiaryListVo> diaries;

		if (specialtyId == null)
			if (healthcareProfessionalId.equals(associatedHealthcareProfessionalId) || associatedHealthcareProfessionalId == null || loggedUserExternalService.hasAnyRoleInstitution(institutionId, ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRATIVO))
				diaries = diaryRepository.getActiveDiariesFromProfessional(healthcareProfessionalId, institutionId);
			else
				diaries = diaryRepository.getActiveAssociatedDiariesFromProfessional(associatedHealthcareProfessionalId, healthcareProfessionalId, institutionId);
		else
			if (healthcareProfessionalId.equals(associatedHealthcareProfessionalId) || associatedHealthcareProfessionalId == null)
				diaries = diaryRepository.getActiveDiariesFromProfessionalAndSpecialty(healthcareProfessionalId, specialtyId, institutionId);
			else
				diaries = diaryRepository.getActiveAssociatedDiariesFromProfessionalAndSpecialty(associatedHealthcareProfessionalId, healthcareProfessionalId, specialtyId, institutionId);

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
		result.setAlias(diaryListVo.getAlias());
		result.setClinicalSpecialtyName(diaryListVo.getClinicalSpecialtyName());
		result.setPredecessorProfessionalId(diaryListVo.getPredecessorProfessionalId());
		result.setHierarchicalUnitId(diaryListVo.getHierarchicalUnitId());
		LOG.debug(OUTPUT, result);
		return result;
	}

	private CompleteDiaryBo createCompleteDiaryBoInstance(CompleteDiaryListVo completeDiaryListVo) {
		LOG.debug("Input parameters -> diaryListVo {}", completeDiaryListVo);
		CompleteDiaryBo result = new CompleteDiaryBo(createDiaryBoInstance(completeDiaryListVo));
		result.setSectorId(completeDiaryListVo.getSectorId());
		result.setSectorDescription(completeDiaryListVo.getSectorDescription());
		result.setClinicalSpecialtyId(completeDiaryListVo.getClinicalSpecialtyId());
		result.setHealthcareProfessionalId(completeDiaryListVo.getHealthcareProfessionalId());
		result.setDoctorsOfficeDescription(completeDiaryListVo.getDoctorsOfficeDescription());
		result.setDoctorFirstName(completeDiaryListVo.getDoctorFirstName());
		result.setDoctorLastName(completeDiaryListVo.getDoctorLastName());
		result.setDoctorMiddleNames(completeDiaryListVo.getDoctorMiddleNames());
		result.setDoctorOtherLastNames(completeDiaryListVo.getDoctorOtherLastNames());
		result.setDoctorNameSelfDetermination(completeDiaryListVo.getDoctorNameSelfDetermination());
		result.setHierarchicalUnitId(completeDiaryListVo.getHierarchicalUnitId());
		result.setHierarchicalUnitAlias(completeDiaryListVo.getHierarchicalUnitAlias());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<CompleteDiaryBo> getDiary(Integer diaryId) {
		LOG.debug(INPUT_DIARY_ID, diaryId);
		Optional<CompleteDiaryBo> result = diaryRepository.getDiary(diaryId).map(this::createCompleteDiaryBoInstance)
				.map(completeOpeningHours());
		result.ifPresent(completeDiaryBo -> {
			completeDiaryBo.setAssociatedProfessionalsInfo(diaryAssociatedProfessionalService.getAllDiaryAssociatedProfessionalsInfo(diaryId));
			completeDiaryBo.setCareLinesInfo(diaryCareLineService.getAllCareLinesByDiaryId(diaryId, completeDiaryBo.getHealthcareProfessionalId()));
			completeDiaryBo.setPracticesInfo(diaryPracticeService.getAllByDiaryId(diaryId));
		});
		LOG.debug(OUTPUT, result);
		return result;
	}

	private Function<CompleteDiaryBo, CompleteDiaryBo> completeOpeningHours() {
		return completeDiary -> {
			Collection<DiaryOpeningHoursBo> diaryOpeningHours = diaryOpeningHoursService
					.getDiariesOpeningHours(Stream.of(completeDiary.getId()).collect(toList()));
			completeDiary.setDiaryOpeningHours(new ArrayList<>(diaryOpeningHours));
			return completeDiary;
		};
	}

	@Override
	public DiaryBo getDiaryById(Integer diaryId) {
		LOG.debug(INPUT_DIARY_ID, diaryId);
		Diary resultQuery = diaryRepository.findById(diaryId)
				.orElseThrow(() -> new NotFoundException("diaryId", "diaryId -> " + diaryId + " does not exist"));
		DiaryBo result = createDiaryBoInstance(resultQuery);
		result.setDiaryAssociatedProfessionalsId(diaryAssociatedProfessionalService.getAllDiaryAssociatedProfessionalsInfo(diaryId)
				.stream().map(ProfessionalPersonBo::getId)
				.collect(toList()));
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Integer getInstitution(Integer diaryId) {
		return diaryRepository.getInstitutionIdByDiary(diaryId)
				.orElseThrow(()-> new DiaryNotFoundException(DiaryNotFoundEnumException.DIARY_ID_NOT_FOUND,"La agenda solicitada no existe"));
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

	@Override
	public Optional<CompleteDiaryBo> getCompleteDiaryByAppointment(Integer appointmentId){
		LOG.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<CompleteDiaryBo> result = diaryRepository.getCompleteDiaryByAppointment(appointmentId).map(this::createCompleteDiaryBoInstance);
		LOG.debug(OUTPUT, result);
		return result;

	}

	@Override
	public Boolean hasPractices(Integer diaryId) {
		LOG.debug("Input parameters -> diaryId {},", diaryId);
		return diaryPracticeService.hasPractice(diaryId);
	}

	@Override
	public Boolean hasActiveDiariesInInstitution(Integer healthcareProfessionalId, Integer institutionId){
		LOG.debug("Input parameters -> healthcareProfessionalId {}, institutionId {}", healthcareProfessionalId, institutionId);
		Boolean result = diaryRepository.hasActiveDiariesInInstitution(healthcareProfessionalId, institutionId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<String> getActiveDiariesAliases(Integer institutionId) {
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		List<String> result = diaryRepository.getActiveDiariesAliases(institutionId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<EmptyAppointmentBo> getEmptyAppointmentsBySearchCriteria(Integer institutionId, AppointmentSearchBo searchCriteria, Boolean mustFilterByModality) {
		LOG.debug("Input parameters -> institutionId {}, searchCriteria {}, mustFilterByModality {}", institutionId, searchCriteria, mustFilterByModality);
		List<EmptyAppointmentBo> emptyAppointments = new ArrayList<>();
		validateSearchCriteria(searchCriteria);
		List<CompleteDiaryBo> diaries =  getActiveDiariesByAliasOrClinicalSpecialtyNameOrPracticeId(institutionId, searchCriteria.getAliasOrSpecialtyName(), searchCriteria.getPracticeId());

		if (mustFilterByModality)
			filterOpeningHoursByModality(searchCriteria, diaries);
		LocalDateTime currentDateTime = dateTimeProvider.nowDateTimeWithZone(institutionExternalService.getTimezone(institutionId));
		for (CompleteDiaryBo diary: diaries)
			emptyAppointments = getEmptyAppointmentBos(searchCriteria, emptyAppointments, diary, currentDateTime);
		emptyAppointments.sort(Comparator.comparing(EmptyAppointmentBo::getDate).thenComparing(EmptyAppointmentBo::getHour));
		LOG.debug(OUTPUT, emptyAppointments);
		return emptyAppointments;
	}

	private void filterOpeningHoursByModality(AppointmentSearchBo searchCriteria, List<CompleteDiaryBo> diariesBySpecialty) {
		diariesBySpecialty.forEach(diary -> diary.setDiaryOpeningHours(diary.getDiaryOpeningHours().stream().filter(openingHours -> (searchCriteria.getModality().equals(EAppointmentModality.ON_SITE_ATTENTION) && openingHours.getOnSiteAttentionAllowed()) ||
				(searchCriteria.getModality().equals(EAppointmentModality.PATIENT_VIRTUAL_ATTENTION) && openingHours.getPatientVirtualAttentionAllowed())).collect(toList())));
	}

	private List<EmptyAppointmentBo> getEmptyAppointmentBos(AppointmentSearchBo searchCriteria,
															List<EmptyAppointmentBo> emptyAppointments,
															CompleteDiaryBo diary,
															LocalDateTime currentDateTime) {
		Collection<AppointmentBo> assignedAppointments = appointmentService.getAppointmentsByDiaries(List.of(diary.getId()), searchCriteria.getInitialSearchDate(), searchCriteria.getEndingSearchDate());
		List<EmptyAppointmentBo> availableAppointments = getDiaryAvailableAppointments(diary, searchCriteria, assignedAppointments, currentDateTime);
		emptyAppointments.addAll(availableAppointments);
		return emptyAppointments;
	}


	private List<CompleteDiaryBo> getActiveDiariesByAliasOrClinicalSpecialtyNameOrPracticeId(Integer institutionId, String aliasOrClinicalSpecialtyName, Integer practiceId) {
		LOG.debug("Input parameters -> institutionId {}, aliasOrClinicalSpecialtyName {}, practice {}", institutionId, aliasOrClinicalSpecialtyName, practiceId);
		if (aliasOrClinicalSpecialtyName != null && practiceId != null)
			 return diaryRepository.getActiveDiariesByAliasOrClinicalSpecialtyNameAndPracticeId(institutionId, aliasOrClinicalSpecialtyName, practiceId).stream()
					 .map(this::createCompleteDiaryBoInstanceWithPractice)
					 .map(cd -> completeOpeningHoursByMedicalAttentionType(cd, MedicalAttentionType.PROGRAMMED))
					 .collect(toList());

		if (aliasOrClinicalSpecialtyName != null && practiceId == null)
			return diaryRepository.getActiveDiariesByAliasOrClinicalSpecialtyName(institutionId, aliasOrClinicalSpecialtyName).stream()
					.map(this::createCompleteDiaryBoInstance)
					.map(cd -> completeOpeningHoursByMedicalAttentionType(cd, MedicalAttentionType.PROGRAMMED))
					.collect(toList());

		return diaryRepository.getActiveDiariesByPracticeId(institutionId, practiceId).stream()
				.map(this::createCompleteDiaryBoInstanceWithPractice)
				.map(cd -> completeOpeningHoursByMedicalAttentionType(cd, MedicalAttentionType.PROGRAMMED))
				.collect(toList());
	}

	private List<EmptyAppointmentBo> getDiaryAvailableAppointments(CompleteDiaryBo diary,
																   AppointmentSearchBo searchCriteria,
																   Collection<AppointmentBo> assignedAppointments,
																   LocalDateTime currentDateTime) {
		List<EmptyAppointmentBo> result = new ArrayList<>();
		Map<Short, Map<Integer, List<LocalTime>>> potentialAppointmentTimesByDay = new HashMap<>();
		diary.getDiaryOpeningHours().forEach(openingHours -> {
			if (searchCriteria.getDaysOfWeek().contains(openingHours.getOpeningHours().getDayWeekId())) {
				potentialAppointmentTimesByDay.computeIfAbsent(openingHours.getOpeningHours().getDayWeekId(), k -> new HashMap<>());
				potentialAppointmentTimesByDay.get(openingHours.getOpeningHours().getDayWeekId())
						.put(openingHours.getOpeningHours().getId(), generateEmptyAppointmentsHoursFromOpeningHours(openingHours.getOpeningHours(), diary, searchCriteria));
			}
		});
		LocalDate searchInitialDate = searchCriteria.getInitialSearchDate();
		LocalDate searchEndingDate = searchCriteria.getEndingSearchDate();
		List<LocalDate> daysBetweenLimits = searchInitialDate.datesUntil(searchEndingDate).collect(Collectors.toList());
		daysBetweenLimits.add(searchEndingDate);
		daysBetweenLimits.forEach(day -> {
			if (day.compareTo(diary.getStartDate()) >= 0 && day.compareTo(diary.getEndDate()) <= 0 && result.size() < 20) {
				generateDayEmptyAppointments(diary, result, potentialAppointmentTimesByDay, day, assignedAppointments, currentDateTime);
			}
		});
		return result;
	}

	private void generateDayEmptyAppointments(CompleteDiaryBo diary,
											  List<EmptyAppointmentBo> result,
											  Map<Short, Map<Integer, List<LocalTime>>> potentialAppointmentTimesByDay,
											  LocalDate day,
											  Collection<AppointmentBo> assignedAppointments,
											  LocalDateTime currentDateTime) {
		int currentDayOfWeek = day.getDayOfWeek().getValue() == 7 ? 0 : day.getDayOfWeek().getValue();
		Map<Integer, List<LocalTime>> emptyAppointmentTimesOfCurrentDayOpeningHours = potentialAppointmentTimesByDay.get((short) currentDayOfWeek);
		if (emptyAppointmentTimesOfCurrentDayOpeningHours != null) {
			emptyAppointmentTimesOfCurrentDayOpeningHours.forEach((openingHoursId, openingHoursTimeList) ->
					result.addAll(openingHoursTimeList.stream()
							.filter(time -> {
								if (day.compareTo(currentDateTime.toLocalDate()) > 0)
									return true;
								else
									return time.compareTo(currentDateTime.toLocalTime()) > 0;
							})
							.map(time -> createEmptyAppointmentBoFromRawData(time, day, diary, openingHoursId))
							.filter(emptyAppointment -> {
								var time = emptyAppointment.getHour();
								var date = emptyAppointment.getDate();
								return assignedAppointments.stream().filter(appointment -> appointment.getDate().equals(date) && appointment.getHour().equals(time)).findAny().isEmpty();
							})
							.collect(Collectors.toList())));
		}
	}

	private List<LocalTime> generateEmptyAppointmentsHoursFromOpeningHours(OpeningHoursBo openingHours, CompleteDiaryBo diary, AppointmentSearchBo searchCriteria) {
		LocalTime searchCriteriaInitialTime = searchCriteria.getInitialSearchTime();
		long iterationAmount = ChronoUnit.MINUTES.between(searchCriteriaInitialTime, searchCriteria.getEndSearchTime()) / diary.getAppointmentDuration();
		List<LocalTime> generatedHours = new ArrayList<>();
		for (int currentEmptyAppointment = 0; currentEmptyAppointment < iterationAmount; currentEmptyAppointment++) {
			if (searchCriteriaInitialTime.compareTo(openingHours.getFrom()) >= 0 && searchCriteriaInitialTime.compareTo(openingHours.getTo()) < 0) {
				generatedHours.add(searchCriteriaInitialTime);
			}
			searchCriteriaInitialTime = searchCriteriaInitialTime.plusMinutes(diary.getAppointmentDuration());
		}
		return generatedHours;
	}

	private EmptyAppointmentBo createEmptyAppointmentBoFromRawData(LocalTime emptyAppointmentTime, LocalDate emptyAppointmentDate, CompleteDiaryBo diary, Integer openingHoursId) {
		EmptyAppointmentBo result = new EmptyAppointmentBo(diary.getDoctorLastName(),diary.getDoctorOtherLastNames(),diary.getDoctorFirstName(),diary.getDoctorMiddleNames(),diary.getDoctorNameSelfDetermination());
		result.setDiaryId(diary.getId());
		result.setDate(emptyAppointmentDate);
		result.setHour(emptyAppointmentTime);
		result.setOverturnMode(false);
		result.setPatientId(null);
		result.setOpeningHoursId(openingHoursId);
		result.setDoctorsOfficeDescription(diary.getDoctorsOfficeDescription());
		result.setClinicalSpecialtyName(diary.getClinicalSpecialtyName());
		result.setAlias(diary.getAlias());
		return result;
	}

	private void validateDiary(DiaryBo diaryBo) {
		if (diaryBo.getPredecessorProfessionalId() != null && diaryBo.getHierarchicalUnitId() == null)
			throw new DiaryException(DiaryEnumException.PREDECESSOR_PROFESSIONAL_WITHOUT_HIERARCHICAL_UNIT,
					"No se puede ingresar un profesional a reemplazar sin seleccionar la unidad jerárquica a la que pertenece");
		diaryBo.getDiaryOpeningHours().forEach(openingHour -> {
			if (openingHour.getOnSiteAttentionAllowed() == null && openingHour.getPatientVirtualAttentionAllowed() == null && openingHour.getSecondOpinionVirtualAttentionAllowed() == null)
				throw new DiaryException(DiaryEnumException.MODALITY_NOT_FOUND,	"Una de las franjas horarias no cuenta con una modalidad definida");
		});
	}

	private CompleteDiaryBo createCompleteDiaryBoInstanceWithPractice(CompleteDiaryListVo vo) {
		var bo = this.createCompleteDiaryBoInstance(vo);
		bo.setPracticesInfo(diaryPracticeService.getAllByDiaryId(vo.getId()));
		return bo;
	}

	private void validateSearchCriteria(AppointmentSearchBo searchCriteria) {
		if (searchCriteria.getAliasOrSpecialtyName() == null && searchCriteria.getPracticeId() == null) {
			throw new DiaryException(DiaryEnumException.SEARCH_CRITERIA_NOT_FOUND,
					"No se puede realizar la búsqueda sin seleccionar el tipo de atención ");
		}
	}
	
	private CompleteDiaryBo completeOpeningHoursByMedicalAttentionType(CompleteDiaryBo completeDiary, short medicalAttentionTypeId) {
		Collection<DiaryOpeningHoursBo> diaryOpeningHours = diaryOpeningHoursService
				.getDiariesOpeningHoursByMedicalAttentionType(Stream.of(completeDiary.getId()).collect(toList()), medicalAttentionTypeId);
		completeDiary.setDiaryOpeningHours(new ArrayList<>(diaryOpeningHours));
		return completeDiary;
	}

}
