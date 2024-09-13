package net.pladema.medicalconsultation.appointment.application;

import java.util.Collection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentPort;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.diary.domain.UpdateDiaryBo;
import net.pladema.medicalconsultation.diary.domain.UpdateDiaryOpeningHoursBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateAppointmentOpeningHours {

    private final AppointmentPort appointmentPort;

    @Transactional
    public void run(UpdateDiaryBo updateDiaryBo) {
        log.debug("Input parameters -> updateDiaryBo {}", updateDiaryBo);

        var result = updateDiaryBo.getUpdateDiaryOpeningHours()
                .stream()
                .map(UpdateDiaryOpeningHoursBo::getAppointments)
                .flatMap(Collection::stream)
                .peek(this::updateAppointment)
                .count();

        log.debug("Output -> updated {} appointments", result);
    }

    private void updateAppointment(UpdateDiaryAppointmentBo appointmentBo) {
        appointmentPort.updateOpeningHoursId(appointmentBo.getOpeningHoursId(), appointmentBo.getId());

        if (AppointmentState.OUT_OF_DIARY == appointmentBo.getStateId()) {
            this.reAssignedAppointmentsThatNowAreValid(appointmentBo);
        }
    }

    private void reAssignedAppointmentsThatNowAreValid(UpdateDiaryAppointmentBo appointmentBo) {
            if (appointmentBo.getPatientId() != null)
                appointmentPort.updateAppointmentState(appointmentBo.getId(), AppointmentState.ASSIGNED);
            else
                appointmentPort.updateAppointmentState(appointmentBo.getId(), AppointmentState.BOOKED);
    }
}
