package net.pladema.medicalconsultation.diary.application;

import ar.lamansys.sgx.shared.security.UserInfo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.domain.IDiaryBo;
import net.pladema.medicalconsultation.diary.domain.IDiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.domain.IOpeningHoursBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateOutOfBoundsAppointments {

    private final AppointmentService appointmentService;

    @Transactional
    public void run(IDiaryBo diaryToUpdate) {
        log.debug("Input parameters -> IDiaryBo {}", diaryToUpdate);

        Collection<AppointmentBo> appointments = appointmentService.getAppointmentsByDiaries(List.of(diaryToUpdate.getId()), null, null);

        LocalDate from = diaryToUpdate.getStartDate();
        LocalDate to = diaryToUpdate.getEndDate();

        HashMap<Short, List<IDiaryOpeningHoursBo>> appointmentsByWeekday = diaryToUpdate.getIDiaryOpeningHours()
                .stream()
                .collect(groupingBy(doh -> doh.getIOpeningHours().getDayWeekId(),
                        HashMap<Short, List<IDiaryOpeningHoursBo>>::new, Collectors.toList()));

        appointments.stream()
                .filter(a -> {
                    List<IDiaryOpeningHoursBo> newHours = appointmentsByWeekday.get(getWeekDay(a.getDate()));
                    return newHours == null
                            || outOfDiaryBounds(from, to, a)
                            || outOfOpeningHoursBounds(a, newHours);
                })
                .forEach(this::changeToOutOfDiaryState);

        log.debug("Output -> updated appointments with out-of-diary state");
    }

    private void changeToOutOfDiaryState(AppointmentBo appointmentBo) {
        if (AppointmentState.BLOCKED == appointmentBo.getAppointmentStateId()) {
            appointmentService.delete(appointmentBo);
            return;
        }
        if (appointmentBo.getDate().isAfter(LocalDate.now()))
            appointmentService.updateState(appointmentBo.getId(), AppointmentState.OUT_OF_DIARY, UserInfo.getCurrentAuditor(), "Fuera de agenda");
    }

    private boolean outOfOpeningHoursBounds(AppointmentBo a, List<IDiaryOpeningHoursBo> newHours) {
        return newHours.stream().noneMatch(newOH -> fitsIn(a, newOH.getIOpeningHours()) && sameMedicalAttention(a, newOH));
    }

    private boolean sameMedicalAttention(AppointmentBo a, IDiaryOpeningHoursBo newOH) {
        return newOH.getMedicalAttentionTypeId().equals(a.getMedicalAttentionTypeId());
    }

    private boolean outOfDiaryBounds(LocalDate from, LocalDate to, AppointmentBo a) {
        return !isBetween(from, to, a);
    }

    private boolean isBetween(LocalDate from, LocalDate to, AppointmentBo a) {
        return !a.getDate().isBefore(from) && !a.getDate().isAfter(to);
    }

    private boolean fitsIn(AppointmentBo appointment, IOpeningHoursBo openingHours) {
        LocalTime from = openingHours.getFrom();
        LocalTime to = openingHours.getTo();
        return (appointment.getHour().equals(from) || appointment.getHour().isAfter(from)) && appointment.getHour().isBefore(to);
    }

}
