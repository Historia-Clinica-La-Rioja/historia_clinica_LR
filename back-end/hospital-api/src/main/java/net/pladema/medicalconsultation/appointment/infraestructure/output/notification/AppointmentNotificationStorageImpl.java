package net.pladema.medicalconsultation.appointment.infraestructure.output.notification;

import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.AppointmentNotificationStorage;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.NotifyPatientBo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentNotificationStorageImpl implements AppointmentNotificationStorage {
    @Override
    public Optional<NotifyPatientBo> fetchNotifyPatientData(Integer appointmentId) {
        return Optional.empty();
    }
}
