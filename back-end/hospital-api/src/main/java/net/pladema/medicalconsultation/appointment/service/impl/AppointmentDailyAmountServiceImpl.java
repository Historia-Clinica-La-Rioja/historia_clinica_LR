package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.service.AppointmentDailyAmountService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDailyAmountBo;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.HolidaysService;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.HolidayBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundException;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;
import ar.lamansys.sgx.shared.dates.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentDailyAmountServiceImpl implements AppointmentDailyAmountService {

    private final AppointmentService appointmentService;
    private final DiaryService diaryService;
	private final HolidaysService holidaysService;

    @Override
    public Collection<AppointmentDailyAmountBo> getDailyAmounts(Integer diaryId, LocalDate from, LocalDate to) {
		log.debug("Input parameter -> diaryId {}", diaryId);

        Collection<AppointmentDailyAmountBo> result = new ArrayList<>();

        CompleteDiaryBo diary = diaryService.getDiary(diaryId)
               .orElseThrow(() -> new DiaryNotFoundException(DiaryNotFoundEnumException.DIARY_ID_NOT_FOUND, "La agenda solicitada no existe"));
		List<HolidayBo> holidays = holidaysService.getHolidays(from, to);

        if (diary.getEndDate().isBefore(to))
            to = diary.getEndDate();
        if (diary.getStartDate().isAfter(from))
            from = diary.getStartDate();

        Collection<AppointmentBo> appointments = appointmentService.getAppointmentsByDiaries(Collections.singletonList(diaryId), from, to);

        for (LocalDate date = from
             ; date.isBefore(to) || date.isEqual(to)
                ; date = date.plusDays(1)) {

            Collection<DiaryOpeningHoursBo> openingHoursDate = this.getOpeningHoursFor(date, diary.getDiaryOpeningHours());

            if (!openingHoursDate.isEmpty()) {
                AppointmentDailyAmountBo dailyAmount = this.calculateAmountsFor(date, openingHoursDate, appointments, diary.getAppointmentDuration(), holidays);
                result.add(dailyAmount);
            }
        }

		log.debug("Output size -> {}", result.size());
		log.trace("Output -> {}", result);
        return result;
    }

    private Collection<DiaryOpeningHoursBo> getOpeningHoursFor(LocalDate date, Collection<DiaryOpeningHoursBo> openingHours) {
        return openingHours.stream()
                .filter(oh -> oh.getOpeningHours().getDayWeekId().equals(DateUtils.getWeekDay(date)))
                .collect(Collectors.toList());
    }

    private AppointmentDailyAmountBo calculateAmountsFor(LocalDate date,
                                                         Collection<DiaryOpeningHoursBo> openingHours,
                                                         Collection<AppointmentBo> appointments,
                                                         Short appointmentDuration,
														 List<HolidayBo> holidays) {
        AppointmentDailyAmountBo result = new AppointmentDailyAmountBo(null, null, null, null, date);
        Collection<AppointmentBo> appointmentsForDate = appointments.stream()
                .filter(a -> a.getDate().equals(date))
                .collect(Collectors.toList());

        openingHours.forEach(oh -> {

            var appointmentsInOH = appointmentsForDate.stream()
                    .filter(oh::fitsAppointmentHere)
                    .filter(a -> !a.isOverturn())
                    .collect(Collectors.toList());

            if (oh.getMedicalAttentionTypeId().equals(MedicalAttentionType.SPONTANEOUS)) {
                calculateSpontaneousAmount(result, appointmentsInOH);
            } else {
                calculateProgrammedAmount(appointmentDuration, result, appointmentsInOH, oh);
            }
        });

        calculateHolidaysAmount(date, holidays, result);

        return result;
    }

    private void calculateHolidaysAmount(LocalDate date, List<HolidayBo> holidays, AppointmentDailyAmountBo result) {
        List<HolidayBo> currentDayHolidays = holidays.stream()
                .filter(holiday -> holiday.getDate().equals(date))
                .collect(Collectors.toList());
        if (!currentDayHolidays.isEmpty())
            result.setHoliday(currentDayHolidays.size());
    }

    private void calculateProgrammedAmount(Short appointmentDuration,
                                           AppointmentDailyAmountBo dailyAmountBo,
                                           Collection<AppointmentBo> appointmentsFromOpeningHours,
                                           DiaryOpeningHoursBo oh) {
        Long diff = MINUTES.between(oh.getOpeningHours().getFrom(), oh.getOpeningHours().getTo());
        Long segmentsInOpeningHour = diff / appointmentDuration;

        if (dailyAmountBo.getProgrammed() == null) {
            dailyAmountBo.setProgrammed(0);
            dailyAmountBo.setProgrammedAvailable(0);
        }

        var blockedAppointments = (int) appointmentsFromOpeningHours.stream()
                .filter(AppointmentBo::isBlocked)
                .count();
        var validAppointments = (int) appointmentsFromOpeningHours.stream()
                .filter(a -> !a.isBlocked())
                .count();

        int dailyAccumulatedProgrammedAvailableOpeningHours = dailyAmountBo.getProgrammedAvailable();
        int dailyAccumulatedProgrammedOpeningHours = dailyAmountBo.getProgrammed();

        int segmentsAvailablesToAssign = segmentsInOpeningHour.intValue() - blockedAppointments;

        int programmed = dailyAccumulatedProgrammedOpeningHours + validAppointments;
        int programmedAvailable = dailyAccumulatedProgrammedAvailableOpeningHours + segmentsAvailablesToAssign - validAppointments;

        dailyAmountBo.setProgrammed(programmed);
        dailyAmountBo.setProgrammedAvailable(programmedAvailable);
    }

    private void calculateSpontaneousAmount(AppointmentDailyAmountBo dailyAmountBo,
                                            Collection<AppointmentBo> appointmentsFromOpeningHours) {
        if (dailyAmountBo.getSpontaneous() == null)
            dailyAmountBo.setSpontaneous(0);

        var validAppointments = (int) appointmentsFromOpeningHours.stream()
                .filter(a -> !a.isBlocked())
                .count();
        var dailyAccumulatedSpontaneousAppointments = dailyAmountBo.getSpontaneous();
        var spontaneousTotal = dailyAccumulatedSpontaneousAppointments + validAppointments;
        dailyAmountBo.setSpontaneous(spontaneousTotal);
    }
}
