package net.pladema.medicalconsultation.diary.application;

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

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Slf4j
@Service
public class GetMonthlyFreeAppointmentDates {

	private DiaryRepository diaryRepository;

	private AppointmentService appointmentService;

	private DiaryOpeningHoursService diaryOpeningHoursService;

	private FreeAppointmentsUtils freeAppointmentsUtils;

	public List<LocalDate> run(Integer diaryId, FreeAppointmentSearchFilterBo filter) {
		log.debug("Input parameters -> diaryId {}, filter {}", diaryId, filter);
		assertValidDate(diaryId, filter.getDate());
		DiaryBo diary = diaryRepository.getDiaryEndDateAndAppointmentDuration(diaryId);
		LocalDate endDate = getLastCalculationDate(filter.getDate(), diary);
		Collection<AppointmentBo> assignedAppointments = appointmentService.getAppointmentsByDiaries(List.of(diaryId), filter.getDate(), endDate);
		Collection<DiaryOpeningHoursBo> openingHours = diaryOpeningHoursService.getDiaryOpeningHours(diaryId);
		List<LocalDate> result = new ArrayList<>();
		openingHours.forEach(diaryOpeningHoursBo -> processOpeningHours(filter, diaryOpeningHoursBo, diary, endDate, assignedAppointments, result));
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertValidDate(Integer diaryId, LocalDate date) {
		DiaryBo diaryLimitDates = diaryRepository.getDiaryStartAndEndDate(diaryId);
		Assert.isTrue((date.equals(diaryLimitDates.getEndDate()) || date.isBefore(diaryLimitDates.getEndDate())) && (date.equals(diaryLimitDates.getStartDate()) || date.isAfter(diaryLimitDates.getStartDate())), "La fecha solicitada se encuentra fuera del rango definido por la agenda");
	}

	private void processOpeningHours(FreeAppointmentSearchFilterBo filter, DiaryOpeningHoursBo diaryOpeningHoursBo, DiaryBo diary, LocalDate endDate, Collection<AppointmentBo> assignedAppointments, List<LocalDate> result) {
		boolean isOpeningHoursValidToReassign = freeAppointmentsUtils.isOpeningHoursValidToReassign(filter, diaryOpeningHoursBo);
		if (isOpeningHoursValidToReassign)
			processOpeningHoursFreeAppointments(filter.getDate(), diaryOpeningHoursBo, diary.getAppointmentDuration(), endDate, assignedAppointments, result);
	}

	private LocalDate getLastCalculationDate(LocalDate startDate, DiaryBo diary) {
		YearMonth yearMonth = YearMonth.of(startDate.getYear(), startDate.getMonth());
        return yearMonth.atEndOfMonth().isAfter(diary.getEndDate()) ? diary.getEndDate() : yearMonth.atEndOfMonth();
	}

	private void processOpeningHoursFreeAppointments(LocalDate startDate, DiaryOpeningHoursBo diaryOpeningHoursBo, Short appointmentDuration, LocalDate endDate,
													 Collection<AppointmentBo> assignedAppointments, List<LocalDate> result) {
		LocalTime openingHoursStartTime = diaryOpeningHoursBo.getOpeningHours().getFrom();
		LocalTime openingHoursEndTime = diaryOpeningHoursBo.getOpeningHours().getTo();
		long possibleAppointmentsAmount = ChronoUnit.MINUTES.between(openingHoursStartTime, openingHoursEndTime) / appointmentDuration + diaryOpeningHoursBo.getOverturnCount();
		int openingHoursDayOfWeek = freeAppointmentsUtils.parseOpeningHoursWeekOfDay(diaryOpeningHoursBo.getOpeningHours());
		LocalDate iterationWeekDay = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(openingHoursDayOfWeek)));
		while (iterationWeekDay.isBefore(endDate) || iterationWeekDay.equals(endDate)) {
			processDateInSearchOfFreeAppointments(assignedAppointments, result, iterationWeekDay, possibleAppointmentsAmount);
			iterationWeekDay = iterationWeekDay.plusWeeks(1);
		}
	}

	private void processDateInSearchOfFreeAppointments(Collection<AppointmentBo> assignedAppointments, List<LocalDate> result, LocalDate iterationWeekDay, long possibleAppointmentsAmount) {
        List<AppointmentBo> dayAppointments = assignedAppointments.stream().filter(appointment -> appointment.getDate().equals(iterationWeekDay)).collect(toList());
		if (dayAppointments.size() < possibleAppointmentsAmount && !result.contains(iterationWeekDay))
			result.add(iterationWeekDay);
	}

}
