package net.pladema.medicalconsultation.diary.application;

import java.time.LocalDate;
import java.util.List;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentPort;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryPort;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HandleDiaryOutOfBoundsAppointments {

	private final AppointmentService appointmentService;
    private final AppointmentPort appointmentPort;
	private final DiaryPort diaryPort;

    @Transactional
    public void run(DiaryBo diaryToUpdate) {
        log.debug("Input parameters -> diary {}", diaryToUpdate);

        List<UpdateDiaryAppointmentBo> appointments = diaryPort.getUpdateDiaryAppointments(diaryToUpdate.getId());

        appointments.stream()
                .filter(diaryToUpdate::isAppointmentOutOfDiary)
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

}
