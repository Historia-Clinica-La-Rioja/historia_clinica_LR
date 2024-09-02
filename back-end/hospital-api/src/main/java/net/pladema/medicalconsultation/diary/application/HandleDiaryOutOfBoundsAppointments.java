package net.pladema.medicalconsultation.diary.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentPort;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryPort;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;

import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@RequiredArgsConstructor
@Service
public class HandleDiaryOutOfBoundsAppointments {

	private final AppointmentService appointmentService;

    private final AppointmentPort appointmentPort;

	private final DiaryPort diaryPort;

    @Transactional
    public void run(DiaryBo diaryToUpdate) {
        log.debug("Input parameters -> IDiaryBo {}", diaryToUpdate);

        List<UpdateDiaryAppointmentBo> appointments = diaryPort.getUpdateDiaryAppointments(diaryToUpdate.getId());

        LocalDate from = diaryToUpdate.getStartDate();
        LocalDate to = diaryToUpdate.getEndDate();

        HashMap<Short, List<DiaryOpeningHoursBo>> appointmentsByWeekday = diaryToUpdate.getDiaryOpeningHours()
                .stream()
                .collect(groupingBy(doh -> doh.getOpeningHours().getDayWeekId(), HashMap<Short, List<DiaryOpeningHoursBo>>::new, Collectors.toList()));

        appointments.stream()
                .filter(a -> {
                    List<DiaryOpeningHoursBo> newHours = appointmentsByWeekday.get(getWeekDay(a.getDate()));
                    return newHours == null
                            || outOfDiaryBounds(from, to, a)
                            || outOfOpeningHoursBounds(a, newHours);
                })
                .forEach(this::changeToOutOfDiaryState);

        log.debug("Output -> updated appointments with out-of-diary state");
    }

    private void changeToOutOfDiaryState(UpdateDiaryAppointmentBo appointmentBo) {
        if (AppointmentState.BLOCKED == appointmentBo.getStateId()) {
            appointmentPort.deleteAppointmentById(appointmentBo.getId());
            return;
        }
        if (appointmentBo.getDate().isAfter(LocalDate.now()))
            appointmentService.updateState(appointmentBo.getId(), AppointmentState.OUT_OF_DIARY, UserInfo.getCurrentAuditor(), "Fuera de agenda");
    }

	private boolean outOfOpeningHoursBounds(UpdateDiaryAppointmentBo a, List<DiaryOpeningHoursBo> newHours) {
        return newHours.stream().noneMatch(newOH -> fitsIn(a, newOH.getOpeningHours()) && sameMedicalAttention(a, newOH));
    }

    private boolean sameMedicalAttention(UpdateDiaryAppointmentBo a, DiaryOpeningHoursBo newOH) {
        return newOH.getMedicalAttentionTypeId().equals(a.getMedicalAttentionTypeId());
    }

    private boolean outOfDiaryBounds(LocalDate from, LocalDate to, UpdateDiaryAppointmentBo a) {
        return !isBetween(from, to, a);
    }

    private boolean isBetween(LocalDate from, LocalDate to, UpdateDiaryAppointmentBo a) {
        return !a.getDate().isBefore(from) && !a.getDate().isAfter(to);
    }

    private boolean fitsIn(UpdateDiaryAppointmentBo appointment, OpeningHoursBo openingHours) {
        LocalTime from = openingHours.getFrom();
        LocalTime to = openingHours.getTo();
        return (appointment.getTime().equals(from) || appointment.getTime().isAfter(from)) && appointment.getTime().isBefore(to);
    }

}
