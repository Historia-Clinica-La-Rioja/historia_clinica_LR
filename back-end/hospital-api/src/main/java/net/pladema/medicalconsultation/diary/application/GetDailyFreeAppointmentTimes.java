package net.pladema.medicalconsultation.diary.application;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.domain.FreeAppointmentSearchFilterBo;
import net.pladema.medicalconsultation.diary.domain.service.FreeAppointmentsUtils;
import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursFreeTimesBo;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Slf4j
@Service
public class GetDailyFreeAppointmentTimes {

	private DiaryRepository diaryRepository;

	private AppointmentService appointmentService;

	private DiaryOpeningHoursService diaryOpeningHoursService;

	private FreeAppointmentsUtils freeAppointmentsUtils;

	private FeatureFlagsService featureFlagsService;

	public List<DiaryOpeningHoursFreeTimesBo> run(Integer diaryId, FreeAppointmentSearchFilterBo filter) {
		log.debug("Input parameters -> diaryId {}, filter {}", diaryId, filter);
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_AGENDA_DINAMICA))
			assertValidDate(diaryId, filter.getDate());
		Short diaryAppointmentDuration = diaryRepository.getDiaryAppointmentDuration(diaryId);
		Collection<AppointmentBo> assignedAppointments = appointmentService.getAppointmentsByDiaries(List.of(diaryId), filter.getDate(), filter.getDate());
		Collection<DiaryOpeningHoursBo> openingHours = diaryOpeningHoursService.getDiaryOpeningHours(diaryId);
		List<DiaryOpeningHoursFreeTimesBo> result = openingHours.stream()
				.filter(diaryOpeningHours -> isOpeningHoursValid(filter, diaryOpeningHours))
				.map(diaryOpeningHours -> calculateDiaryOpeningHoursFreeTimes(filter.getDate(), diaryOpeningHours, diaryAppointmentDuration, assignedAppointments))
				.collect(toList());
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertValidDate(Integer diaryId, LocalDate date) {
		DiaryBo diaryLimitDates = diaryRepository.getDiaryStartAndEndDate(diaryId);
		Assert.isTrue((date.equals(diaryLimitDates.getEndDate()) || date.isBefore(diaryLimitDates.getEndDate())) && (date.equals(diaryLimitDates.getStartDate()) || date.isAfter(diaryLimitDates.getStartDate())), "La fecha solicitada se encuentra fuera del rango definido por la agenda");
	}

	private boolean isOpeningHoursValid(FreeAppointmentSearchFilterBo filter, DiaryOpeningHoursBo diaryOpeningHours) {
		int openingHoursDayOfWeek = freeAppointmentsUtils.parseOpeningHoursWeekOfDay(diaryOpeningHours.getOpeningHours());
		return openingHoursDayOfWeek == filter.getDate().getDayOfWeek().getValue() && freeAppointmentsUtils.isOpeningHoursValidToReassign(filter, diaryOpeningHours);
	}

	private DiaryOpeningHoursFreeTimesBo calculateDiaryOpeningHoursFreeTimes(LocalDate date,
																			 DiaryOpeningHoursBo diaryOpeningHours,
																			 Short appointmentDuration,
																			 Collection<AppointmentBo> assignedAppointments) {
		var startTime = diaryOpeningHours.getOpeningHours().getFrom();
		var endTime = diaryOpeningHours.getOpeningHours().getTo();
		List<LocalTime> timeResult = new ArrayList<>();
		LocalTime maxTime = LocalTime.of(0,0);
		startTime = updateTime(appointmentDuration, timeResult, startTime);
		while (startTime.isBefore(endTime) && !startTime.equals(maxTime))
			startTime = updateTime(appointmentDuration, timeResult, startTime);

		var assignedAppointmentsDiaryOpeningHours = assignedAppointments.stream()
				.filter(diaryOpeningHours::fitsAppointmentHere)
				.collect(toList());
		long possibleAppointmentsAmount = getPossibleAppointmentsAmount(diaryOpeningHours, appointmentDuration);

		processTimesInSearchOfFreeAppointments(date, timeResult, diaryOpeningHours, assignedAppointmentsDiaryOpeningHours, possibleAppointmentsAmount);
		return new DiaryOpeningHoursFreeTimesBo(diaryOpeningHours.getOpeningHours().getId(), timeResult);
	}

	private void processTimesInSearchOfFreeAppointments(LocalDate date,
														List<LocalTime> timeResult,
														DiaryOpeningHoursBo diaryOpeningHours,
														Collection<AppointmentBo> assignedDiaryOpeningHoursAppointments,
														long possibleAppointmentsAmount) {

		LocalDate iterationWeekDay = getIterationWeekDayLocalDate(date, diaryOpeningHours);

		List<AppointmentBo> openingHoursAndDayAppointments = assignedDiaryOpeningHoursAppointments.stream()
				.filter(a -> a.getDate().equals(iterationWeekDay))
				.collect(toList());

		int overturnAppointmentsQuantity = (int) openingHoursAndDayAppointments.stream()
				.filter(AppointmentBo::isOverturn)
				.count();

		boolean exceedAvailableAppointmentsAmounts = openingHoursAndDayAppointments.size() >= possibleAppointmentsAmount;
		
		int overturnsCountOpeningHours = diaryOpeningHours.getOverturnCount();

		boolean allowOverturns = overturnsCountOpeningHours != 0;

		boolean exceedOverturns = overturnAppointmentsQuantity >= overturnsCountOpeningHours;

		if (exceedAvailableAppointmentsAmounts || (allowOverturns && exceedOverturns))
			assignedDiaryOpeningHoursAppointments.stream()
					.map(AppointmentBo::getHour)
					.forEach(timeResult::remove);
	}

	private LocalDate getIterationWeekDayLocalDate(LocalDate date, DiaryOpeningHoursBo diaryOpeningHours) {
		int openingHoursDayOfWeek = freeAppointmentsUtils.parseOpeningHoursWeekOfDay(diaryOpeningHours.getOpeningHours());
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(openingHoursDayOfWeek)));
	}

	private long getPossibleAppointmentsAmount(DiaryOpeningHoursBo diaryOpeningHours, Short appointmentDuration) {
		return ChronoUnit.MINUTES.between(diaryOpeningHours.getOpeningHours().getFrom(), diaryOpeningHours.getOpeningHours().getTo()) / appointmentDuration + diaryOpeningHours.getOverturnCount();
	}

	private LocalTime updateTime(Short appointmentDuration, List<LocalTime> time, LocalTime startTime) {
		time.add(startTime);
		startTime = startTime.plusMinutes(appointmentDuration);
		return startTime;
	}

}
