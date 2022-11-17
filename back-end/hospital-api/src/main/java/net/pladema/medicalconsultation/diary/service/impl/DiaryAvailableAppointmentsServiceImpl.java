package net.pladema.medicalconsultation.diary.service.impl;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryProtectedAppointmentsSearch;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.diary.repository.DiaryAvailableProtectedAppointmentsSearchRepository;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableProtectedAppointmentsBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableProtectedAppointmentsInfoBo;
import net.pladema.medicalconsultation.diary.service.DiaryAvailableAppointmentsService;

import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;

import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;

import org.springframework.stereotype.Service;


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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryAvailableAppointmentsServiceImpl implements DiaryAvailableAppointmentsService {

	private static final String OUTPUT = "Output -> {}";

	public final int WEEK_DAY_NUMBER = 7;

	private final DiaryAvailableProtectedAppointmentsSearchRepository diaryAvailableProtectedAppointmentsSearchRepository;
	private final FeatureFlagsService featureFlagsService;
	private final DiaryOpeningHoursService diaryOpeningHoursService;
	private final InstitutionExternalService institutionExternalService;
	private final AppointmentService appointmentService;
	private final DateTimeProvider dateTimeProvider;
	private final SharedReferenceCounterReference sharedReferenceCounterReference;

	@Override
	public List<DiaryAvailableProtectedAppointmentsBo> getAvailableProtectedAppointmentsBySearchCriteria(DiaryProtectedAppointmentsSearch searchCriteria,
																										 Integer institutionId) {
		log.debug("Input parameter -> diaryProtectedAppointmentsSearch {}", searchCriteria);

		if (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS))
			searchCriteria.setIncludeNameSelfDetermination(true);

		List<DiaryAvailableProtectedAppointmentsInfoBo> diariesInfo = diaryAvailableProtectedAppointmentsSearchRepository.getAllDiaryProtectedAppointmentsByFilter(searchCriteria);
		List<Integer> diaryIds = diariesInfo.stream().map(DiaryAvailableProtectedAppointmentsInfoBo::getDiaryId).collect(Collectors.toList());
		Collection<AppointmentBo> assignedAppointments = appointmentService.getAppointmentsByDiaries(diaryIds, searchCriteria.getInitialSearchDate(), searchCriteria.getEndSearchDate());

		List<DiaryAvailableProtectedAppointmentsBo> result = new ArrayList<>();

		for (DiaryAvailableProtectedAppointmentsInfoBo diaryInfo : diariesInfo) {
			diaryInfo.setOpeningHours(new ArrayList<>(diaryOpeningHoursService.getDiaryOpeningHours(diaryInfo.getDiaryId())));
			result.addAll(getDiaryAvailableAppointments(diaryInfo, assignedAppointments, searchCriteria, institutionId));
		}
		result.sort(Comparator.comparing(DiaryAvailableProtectedAppointmentsBo::getDate).thenComparing(DiaryAvailableProtectedAppointmentsBo::getHour));
		log.debug(OUTPUT, result);
		return result;
	}

	private List<DiaryAvailableProtectedAppointmentsBo> getDiaryAvailableAppointments(DiaryAvailableProtectedAppointmentsInfoBo diaryInfo,
																					  Collection<AppointmentBo> assignedAppointments,
																					  DiaryProtectedAppointmentsSearch searchCriteria,
																					  Integer institutionId) {
		List<DiaryAvailableProtectedAppointmentsBo> result = new ArrayList<>();
		Map<Short, Map<Integer, List<LocalTime>>> potentialAppointmentTimesByDay = new HashMap<>();

		diaryInfo.getOpeningHours().forEach(openingHours -> {
			potentialAppointmentTimesByDay.computeIfAbsent(openingHours.getOpeningHours().getDayWeekId(), k -> new HashMap<>());
			potentialAppointmentTimesByDay.get(openingHours.getOpeningHours().getDayWeekId()).put(openingHours.getOpeningHours().getId(), getAvaiableAppointmentsHoursFromOpeningHours(openingHours.getOpeningHours(), diaryInfo));
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

	private List<LocalTime> getAvaiableAppointmentsHoursFromOpeningHours(OpeningHoursBo openingHours,
																		 DiaryAvailableProtectedAppointmentsInfoBo diary) {
		Long slots = ChronoUnit.MINUTES.between(openingHours.getFrom(), openingHours.getTo()) / diary.getAppointmentDuration();
		List<LocalTime> generatedHours = new ArrayList<>();
		LocalTime hour = openingHours.getFrom();
		for (int currentEmptyAppointment = 0; currentEmptyAppointment < slots; currentEmptyAppointment++) {
			generatedHours.add(hour);
			hour = hour.plusMinutes(diary.getAppointmentDuration());
		}
		return generatedHours;
	}

	private List<DiaryAvailableProtectedAppointmentsBo> getAvailableAppointmentsInDay(DiaryAvailableProtectedAppointmentsInfoBo diaryInfo,
																					  Map<Short, Map<Integer, List<LocalTime>>> potentialAppointmentTimesByDay,
																					  LocalDate day,
																					  Collection<AppointmentBo> assignedAppointments,
																					  Integer institutionId) {
		LocalDateTime currentDateTime = dateTimeProvider.nowDateTimeWithZone(institutionExternalService.getTimezone(institutionId));
		int currentDayOfWeek = day.getDayOfWeek().getValue() == WEEK_DAY_NUMBER ? 0 : day.getDayOfWeek().getValue();

		List<DiaryAvailableProtectedAppointmentsBo> result = new ArrayList<>();
		Map<Integer, List<LocalTime>> availableAppointmentTimes = potentialAppointmentTimesByDay.get((short) currentDayOfWeek);
		if (availableAppointmentTimes != null && hasAvailableProtectedAppointments(availableAppointmentTimes, diaryInfo, day)) {
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

	private boolean hasAvailableProtectedAppointments(Map<Integer, List<LocalTime>> availableAppointmentTimes,
													  DiaryAvailableProtectedAppointmentsInfoBo diaryInfo,
													  LocalDate day) {
		AtomicReference<Integer> times = new AtomicReference<>(0);
		availableAppointmentTimes.forEach((openingHours, openingHoursTimeList) -> {
			times.updateAndGet(t -> t + openingHoursTimeList.size());
		});

		Integer quantityAssigned = sharedReferenceCounterReference.getAssignedProtectedAppointmentsQuantity(diaryInfo.getDiaryId(), day, AppointmentState.CANCELLED);
		double quantityAvailable = Math.round((times.get() * diaryInfo.getProtectedAppointmentsPercentage().doubleValue()) / 100);

		return quantityAssigned < quantityAvailable;
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
		return assignedAppointments.stream().filter(appointment ->
				appointment.getDiaryId().equals(diaryId) && appointment.getDate().equals(date) && appointment.getHour().equals(time)
		).findAny().isEmpty();
	}

	private DiaryAvailableProtectedAppointmentsBo createAvailableProtectedAppointment(LocalTime availableAppointmentTime, LocalDate availableAppointmentDate,
																					  DiaryAvailableProtectedAppointmentsInfoBo diary,
																					  Integer openingHoursId) {
		return DiaryAvailableProtectedAppointmentsBo.builder()
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
				.build();
	}

}