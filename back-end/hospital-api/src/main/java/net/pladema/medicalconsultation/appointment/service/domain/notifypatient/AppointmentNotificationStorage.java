package net.pladema.medicalconsultation.appointment.service.domain.notifypatient;

import java.util.Optional;

public interface AppointmentNotificationStorage {
    Optional<NotifyPatientBo> fetchNotifyPatientData(Integer appointmentId);
}
