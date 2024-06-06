package net.pladema.medicalconsultation.diary.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.appointment.service.domain.EmptyAppointmentBo;
import net.pladema.medicalconsultation.diary.application.exceptions.DiaryAvailableAppointmentsException;
import net.pladema.medicalconsultation.diary.application.exceptions.DiaryAvailableAppointmentsExceptionEnum;
import net.pladema.medicalconsultation.diary.domain.DiaryAppointmentsSearchBo;
import net.pladema.medicalconsultation.diary.repository.DiaryAvailableAppointmentsSearchRepository;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.service.DiaryCareLineService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableAppointmentsBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableAppointmentsInfoBo;
import net.pladema.medicalconsultation.diary.service.DiaryAvailableAppointmentsService;

import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;

import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;

import net.pladema.staff.repository.ClinicalSpecialtyRepository;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class DiaryAvailableAppointmentsServiceImpl implements DiaryAvailableAppointmentsService {

	private static final String OUTPUT = "Output -> {}";

	private static final String OUTPUT_SIZE = "Output size -> {}";

	private static final Integer NO_INSTITUTION = -1;

	private static final int WEEK_DAY_NUMBER = 7;

	private final DiaryAvailableAppointmentsSearchRepository diaryAvailableAppointmentsSearchRepository;

	private final FeatureFlagsService featureFlagsService;

	private final DiaryOpeningHoursService diaryOpeningHoursService;

	private final InstitutionExternalService institutionExternalService;

	private final AppointmentService appointmentService;

	private final DateTimeProvider dateTimeProvider;

	private final DiaryService diaryService;

	private final DiaryCareLineService diaryCareLineService;

	private final ClinicalSpecialtyRepository clinicalSpecialtyRepository;

	private final SnomedService snomedService;

	@Override
	public List<DiaryAvailableAppointmentsBo> getAvailableProtectedAppointmentsBySearchCriteria(DiaryAppointmentsSearchBo searchCriteria, Integer institutionId) {
	log.debug("Input parameter -> searchCriteria {}, institutionId {}", searchCriteria, institutionId);
		if (institutionId.equals(NO_INSTITUTION))
			searchCriteria.setRegulationProtectedAppointment(true);
		else
			searchCriteria.setProtectedAppointment(true);
		List<DiaryAvailableAppointmentsBo> result = this.getAvailableAppointmentsBySearchCriteria(searchCriteria, institutionId);
		log.debug(OUTPUT_SIZE, result.size());
		return result;
	}

	@Override
	public List<DiaryAvailableAppointmentsBo> getAvailableAppointmentsToThirdPartyBooking(DiaryAppointmentsSearchBo searchCriteria, Integer institutionId) {
		log.debug("Input parameter -> searchCriteria {}, institutionId {}", searchCriteria, institutionId);
		assertMinimalThirdPartyBookingSearchData(searchCriteria);
		searchCriteria.setExternalBookingAppointment(true);
		searchCriteria.setModality(EAppointmentModality.ON_SITE_ATTENTION);
		List<DiaryAvailableAppointmentsBo> result = this.getAvailableAppointmentsBySearchCriteria(searchCriteria, institutionId);
		log.debug(OUTPUT_SIZE, result.size());
		return result;
	}

	private List<DiaryAvailableAppointmentsBo> getAvailableAppointmentsBySearchCriteria(DiaryAppointmentsSearchBo searchCriteria, Integer institutionId) {
		Integer practiceId = searchCriteria.getPracticeId();
		if (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS))
			searchCriteria.setIncludeNameSelfDetermination(true);

		List<DiaryAvailableAppointmentsInfoBo> diariesInfo = diaryAvailableAppointmentsSearchRepository.getAllDiaryAppointmentsByFilter(searchCriteria);

		if (searchCriteria.getClinicalSpecialtyIds() != null && !searchCriteria.getClinicalSpecialtyIds().isEmpty() && practiceId == null)
			diariesInfo = diariesInfo.stream().filter( diary -> !diaryService.hasPractices(diary.getDiaryId())).collect(Collectors.toList());

		if (practiceId != null) {
			diariesInfo.stream().forEach( diary -> {
				SnomedBo practice = snomedService.getSnomed(practiceId);
				diary.setPractice(practice);
			});
		}

		List<Integer> diaryIds = diariesInfo.stream().map(DiaryAvailableAppointmentsInfoBo::getDiaryId).collect(Collectors.toList());
		Collection<AppointmentBo> assignedAppointments = appointmentService.getAppointmentsByDiaries(diaryIds, searchCriteria.getInitialSearchDate(), searchCriteria.getEndSearchDate());

		List<DiaryAvailableAppointmentsBo> result = new ArrayList<>();

		for (DiaryAvailableAppointmentsInfoBo diaryInfo : diariesInfo) {
			diaryInfo.setOpeningHours(diaryOpeningHoursService.getDiaryOpeningHours(
							diaryInfo.getDiaryId())
					.stream()
					.filter(doh -> filterByTypeAndModality(searchCriteria, doh))
					.collect(Collectors.toList()));
			result.addAll(getDiaryAvailableAppointments(diaryInfo, assignedAppointments, searchCriteria, institutionId));
		}
		result.sort(Comparator.comparing(DiaryAvailableAppointmentsBo::getDate).thenComparing(DiaryAvailableAppointmentsBo::getHour));
		log.debug(OUTPUT, result);
		return result;
	}

	private boolean filterByTypeAndModality(DiaryAppointmentsSearchBo searchCriteria, DiaryOpeningHoursBo doh) {
		return (
				(searchCriteria.getProtectedAppointment() && doh.getProtectedAppointmentsAllowed() != null && doh.getProtectedAppointmentsAllowed()) ||
						(searchCriteria.getRegulationProtectedAppointment() && doh.getRegulationProtectedAppointmentsAllowed() != null && doh.getRegulationProtectedAppointmentsAllowed()) ||
						(searchCriteria.getExternalBookingAppointment() && doh.getExternalAppointmentsAllowed() != null && doh.getExternalAppointmentsAllowed())
				) &&
				(
						(searchCriteria.getModality().equals(EAppointmentModality.ON_SITE_ATTENTION) && doh.getOnSiteAttentionAllowed()) ||
								(searchCriteria.getModality().equals(EAppointmentModality.PATIENT_VIRTUAL_ATTENTION) && doh.getPatientVirtualAttentionAllowed()) ||
								(searchCriteria.getModality().equals(EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION) && doh.getSecondOpinionVirtualAttentionAllowed()) ||
								searchCriteria.getModality().equals(EAppointmentModality.NO_MODALITY)
				);
	}

	@Override
	public Integer getAvailableAppointmentsBySearchCriteriaQuantity(Integer institutionId, List<Integer> clinicalSpecialtyIds, AppointmentSearchBo searchCriteria) {
		log.debug("Input parameters -> institutionId {}, clinicalSpecialtyIds {}, searchCriteria {}", institutionId, clinicalSpecialtyIds, searchCriteria);
		AtomicInteger result = new AtomicInteger();
		if (clinicalSpecialtyIds != null && !clinicalSpecialtyIds.isEmpty()) {
			List<String> clinicalSpecialtyNames = clinicalSpecialtyRepository.getClinicalSpecialtyNamesByIds(clinicalSpecialtyIds);
			clinicalSpecialtyNames.forEach(clinicalSpecialtyName -> {
				searchCriteria.setAliasOrSpecialtyName(clinicalSpecialtyName);
				result.addAndGet(diaryService.getEmptyAppointmentsBySearchCriteria(institutionId, searchCriteria, false).size());
			});
		}
		else
			result.addAndGet(diaryService.getEmptyAppointmentsBySearchCriteria(institutionId, searchCriteria, false).size());
		log.debug(OUTPUT, result);
		return result.get();
	}

	@Override
	public Integer getAvailableAppointmentsQuantityByCareLineDiaries(Integer institutionId, List<Integer> clinicalSpecialtyIds, AppointmentSearchBo searchCriteria, Integer careLineId) {
		log.debug("Fetch available appointments quantity in diaries based on careline and search criteria, " +
				"input parameters -> institutionId {}, clinicalSpecialtyIds {}, careLineId {}, searchCriteria {} ", institutionId, clinicalSpecialtyIds, careLineId, searchCriteria);
		AtomicInteger result = new AtomicInteger();
		if (clinicalSpecialtyIds != null && !clinicalSpecialtyIds.isEmpty()) {
			List<String> clinicalSpecialtyNames = clinicalSpecialtyRepository.getClinicalSpecialtyNamesByIds(clinicalSpecialtyIds);
			clinicalSpecialtyNames.forEach(clinicalSpecialtyName -> {
				searchCriteria.setAliasOrSpecialtyName(clinicalSpecialtyName);
				result.addAndGet(countAvailableAppointments(institutionId, searchCriteria, careLineId));
			});
		}
		else
			result.addAndGet(countAvailableAppointments(institutionId, searchCriteria, careLineId));
		log.debug(OUTPUT, result.get());
		return result.get();
	}

	private int countAvailableAppointments(Integer institutionId, AppointmentSearchBo searchCriteria, Integer careLineId) {
		List<EmptyAppointmentBo> availableAppointments = diaryService.getEmptyAppointmentsBySearchCriteria(institutionId, searchCriteria, false);
		List<Integer> diariesByCareLineId = diaryCareLineService.getDiaryIdsByCareLineId(careLineId, institutionId);
		return (int) availableAppointments.stream().filter(a -> diariesByCareLineId.contains(a.getDiaryId())).count();
	}

	private List<DiaryAvailableAppointmentsBo> getDiaryAvailableAppointments(DiaryAvailableAppointmentsInfoBo diaryInfo,
																			 Collection<AppointmentBo> assignedAppointments,
																			 DiaryAppointmentsSearchBo searchCriteria,
																			 Integer institutionId) {
		List<DiaryAvailableAppointmentsBo> result = new ArrayList<>();
		Map<Short, Map<Integer, List<LocalTime>>> potentialAppointmentTimesByDay = new HashMap<>();

		diaryInfo.getOpeningHours().forEach(openingHours -> {
			potentialAppointmentTimesByDay.computeIfAbsent(openingHours.getOpeningHours().getDayWeekId(), k -> new HashMap<>());
			potentialAppointmentTimesByDay.get(openingHours.getOpeningHours().getDayWeekId()).put(openingHours.getOpeningHours().getId(), getAvailableAppointmentsHoursFromOpeningHours(openingHours.getOpeningHours(), diaryInfo));
		});

		LocalDate searchInitialDate = searchCriteria.getInitialSearchDate();
		LocalDate searchEndingDate = searchCriteria.getEndSearchDate();
		List<LocalDate> daysBetweenLimits = searchInitialDate.datesUntil(searchEndingDate).collect(Collectors.toList());
		daysBetweenLimits.add(searchEndingDate);
		daysBetweenLimits.forEach(day -> {
			if (day.compareTo(diaryInfo.getStartDate()) >= 0 && day.compareTo(diaryInfo.getEndDate()) <= 0)
				result.addAll(getAvailableAppointmentsInDay(diaryInfo, potentialAppointmentTimesByDay, day, assignedAppointments, institutionId));
		});
		return result;
	}

	private List<LocalTime> getAvailableAppointmentsHoursFromOpeningHours(OpeningHoursBo openingHours,
																		 DiaryAvailableAppointmentsInfoBo diary) {
		Long slots = ChronoUnit.MINUTES.between(openingHours.getFrom(), openingHours.getTo()) / diary.getAppointmentDuration();
		List<LocalTime> generatedHours = new ArrayList<>();
		LocalTime hour = openingHours.getFrom();
		for (int currentEmptyAppointment = 0; currentEmptyAppointment < slots; currentEmptyAppointment++) {
			generatedHours.add(hour);
			hour = hour.plusMinutes(diary.getAppointmentDuration());
		}
		return generatedHours;
	}

	private List<DiaryAvailableAppointmentsBo> getAvailableAppointmentsInDay(DiaryAvailableAppointmentsInfoBo diaryInfo,
																			 Map<Short, Map<Integer, List<LocalTime>>> potentialAppointmentTimesByDay,
																			 LocalDate day,
																			 Collection<AppointmentBo> assignedAppointments,
																			 Integer institutionId) {
		LocalDateTime currentDateTime;
		if(institutionId.equals(NO_INSTITUTION))
			currentDateTime = dateTimeProvider.nowDateTimeWithZone(ZoneId.of("UTC-3"));
		else
			currentDateTime = dateTimeProvider.nowDateTimeWithZone(institutionExternalService.getTimezone(institutionId));

		int currentDayOfWeek = day.getDayOfWeek().getValue() == WEEK_DAY_NUMBER ? 0 : day.getDayOfWeek().getValue();
		List<DiaryAvailableAppointmentsBo> result = new ArrayList<>();
		Map<Integer, List<LocalTime>> availableAppointmentTimes = potentialAppointmentTimesByDay.get((short) currentDayOfWeek);
		if (availableAppointmentTimes != null) {
			availableAppointmentTimes.forEach((openingHoursId, openingHoursTimeList) -> {
				openingHoursTimeList
						.stream()
						.filter(time -> includeHour(currentDateTime, day, time))
						.forEach(time -> {
							if (isPossibleReturnAppointment(assignedAppointments, diaryInfo.getDiaryId(), time, day))
								result.add(createAvailableProtectedAppointment(time, day, diaryInfo, openingHoursId));
				});
			});
		}
		return result;
	}

	private boolean includeHour(LocalDateTime currentDateTime, LocalDate day, LocalTime time) {
		if (day.compareTo(currentDateTime.toLocalDate()) > 0)
			return true;
		else
			return time.compareTo(currentDateTime.toLocalTime()) > 0;
	}

	private boolean isPossibleReturnAppointment(Collection<AppointmentBo> assignedAppointments,
												Integer diaryId,
												LocalTime time,
												LocalDate date) {
		return !assignedAppointments.stream()
				.anyMatch(appointment -> appointment.getDiaryId().equals(diaryId) &&
						appointment.getDate().equals(date) && appointment.getHour().equals(time));
	}

	private DiaryAvailableAppointmentsBo createAvailableProtectedAppointment(LocalTime availableAppointmentTime, LocalDate availableAppointmentDate,
																			 DiaryAvailableAppointmentsInfoBo diary,
																			 Integer openingHoursId) {
		return DiaryAvailableAppointmentsBo.builder()
				.diaryId(diary.getDiaryId())
				.openingHoursId(openingHoursId)
				.overturnMode(false)
				.date(availableAppointmentDate)
				.hour(availableAppointmentTime)
				.institution(diary.getInstitution())
				.department(diary.getDepartment())
				.doctorOffice(diary.getDoctorOffice())
				.clinicalSpecialty(diary.getClinicalSpecialty())
				.professionalFullName(diary.getProfessionalFullName())
				.isJointDiary(diary.getIsJointDiary())
				.practice(diary.getPractice())
				.build();
	}

	private void assertMinimalThirdPartyBookingSearchData(DiaryAppointmentsSearchBo searchCriteria) {
		if (searchCriteria.getDepartmentId() == null)
			throw new DiaryAvailableAppointmentsException(DiaryAvailableAppointmentsExceptionEnum.NULL_DEPARTMENT_ID, "Partido/Departamento es un dato obligatorio para efectuar la búsqueda");
		if (searchCriteria.getHealthcareProfessionalId() == null && searchCriteria.getPracticeId() == null
				&& (searchCriteria.getClinicalSpecialtyIds() == null || searchCriteria.getClinicalSpecialtyIds().isEmpty()))
			throw new DiaryAvailableAppointmentsException(DiaryAvailableAppointmentsExceptionEnum.MINIMAL_SEARCH_DATA, "Para efectuar la búsqueda de turnos debe ingresar al menos especialidad, profesional o practica");
	}

}