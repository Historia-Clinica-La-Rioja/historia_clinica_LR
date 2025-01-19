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
		processOpeningHours(filter, openingHours, diary, endDate, assignedAppointments, result);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertValidDate(Integer diaryId, LocalDate date) {
		DiaryBo diaryLimitDates = diaryRepository.getDiaryStartAndEndDate(diaryId);
		Assert.isTrue((date.equals(diaryLimitDates.getEndDate()) || date.isBefore(diaryLimitDates.getEndDate())) && (date.equals(diaryLimitDates.getStartDate()) || date.isAfter(diaryLimitDates.getStartDate())), "La fecha solicitada se encuentra fuera del rango definido por la agenda");
	}

	private LocalDate getLastCalculationDate(LocalDate startDate, DiaryBo diary) {
		YearMonth yearMonth = YearMonth.of(startDate.getYear(), startDate.getMonth());
        return yearMonth.atEndOfMonth().isAfter(diary.getEndDate()) ? diary.getEndDate() : yearMonth.atEndOfMonth();
	}

	private void processOpeningHours(FreeAppointmentSearchFilterBo filter, Collection<DiaryOpeningHoursBo> diaryOpeningHours,
									 DiaryBo diary, LocalDate endDate, Collection<AppointmentBo> assignedAppointments,
									 List<LocalDate> result) {
		diaryOpeningHours.forEach(doh -> {
			if (freeAppointmentsUtils.isOpeningHoursValidToReassign(filter, doh)) {
				processValidOpeningHours(filter, doh, diary, endDate, assignedAppointments, result);
			}
		});
	}

	private void processValidOpeningHours(FreeAppointmentSearchFilterBo filter, DiaryOpeningHoursBo doh, DiaryBo diary,
										  LocalDate endDate, Collection<AppointmentBo> assignedAppointments,
										  List<LocalDate> result) {
		long possibleAppointmentsAmount = calculatePossibleAppointmentsAmount(doh, diary);
		int openingHoursDayOfWeek = freeAppointmentsUtils.parseOpeningHoursWeekOfDay(doh.getOpeningHours());
		LocalDate iterationWeekDay = filter.getDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.of(openingHoursDayOfWeek)));
		while (iterationWeekDay.isBefore(endDate) || iterationWeekDay.equals(endDate)) {
			processDateInSearchOfFreeAppointments(assignedAppointments, result, iterationWeekDay, doh.getOpeningHours().getId(), possibleAppointmentsAmount);
			iterationWeekDay = iterationWeekDay.plusWeeks(1);
		}
	}

	private long calculatePossibleAppointmentsAmount(DiaryOpeningHoursBo doh, DiaryBo diary) {
		LocalTime openingHoursStartTime = doh.getOpeningHours().getFrom();
		LocalTime openingHoursEndTime = doh.getOpeningHours().getTo();
		return ChronoUnit.MINUTES.between(openingHoursStartTime, openingHoursEndTime) / diary.getAppointmentDuration() + doh.getOverturnCount();
	}

	private void processDateInSearchOfFreeAppointments(Collection<AppointmentBo> assignedAppointments, List<LocalDate> result,
													   LocalDate iterationWeekDay, Integer openingHourId, long possibleAppointmentsAmount) {
        List<AppointmentBo> dayAppointments = assignedAppointments.stream()
				.filter(a -> a.getDate().equals(iterationWeekDay) && a.getOpeningHoursId().equals(openingHourId))
				.collect(toList());
		if (dayAppointments.size() < possibleAppointmentsAmount && !result.contains(iterationWeekDay))
			result.add(iterationWeekDay);
	}

}
