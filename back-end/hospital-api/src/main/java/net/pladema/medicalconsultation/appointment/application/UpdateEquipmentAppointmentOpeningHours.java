package net.pladema.medicalconsultation.appointment.application;

import ar.lamansys.sgx.shared.security.UserInfo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.EquipmentAppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateEquipmentAppointmentOpeningHours {

    private final AppointmentRepository appointmentRepository;
    private final EquipmentAppointmentAssnRepository equipmentAppointmentAssnRepository;

    @Transactional
    public AppointmentBo run(AppointmentBo appointmentBo) {
        log.debug("Input parameters -> appointmentBo {}", appointmentBo);

        equipmentAppointmentAssnRepository.updateOpeningHoursId(appointmentBo.getOpeningHoursId(), appointmentBo.getId());

        if (AppointmentState.OUT_OF_DIARY == appointmentBo.getAppointmentStateId()) {
            if (appointmentBo.getPatientId() != null)
                appointmentRepository.updateState(appointmentBo.getId(), AppointmentState.ASSIGNED, UserInfo.getCurrentAuditor(), LocalDateTime.now());
            else
                appointmentRepository.updateState(appointmentBo.getId(), AppointmentState.BOOKED, UserInfo.getCurrentAuditor(), LocalDateTime.now());
        }

        log.debug("Output -> {}", appointmentBo);
        return appointmentBo;
    }
}

