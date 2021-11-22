package net.pladema.medicalconsultation.appointment.service.notifypatient;

import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.AppointmentNotificationStorage;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.SendAppointmentNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotifyPatient {

    private final AppointmentNotificationStorage appointmentNotificationStorage;
    private final SendAppointmentNotification sendAppointmentNotification;
    private final Logger logger;

    public NotifyPatient(AppointmentNotificationStorage appointmentNotificationStorage, SendAppointmentNotification sendAppointmentNotification) {
        this.appointmentNotificationStorage = appointmentNotificationStorage;
        this.sendAppointmentNotification = sendAppointmentNotification;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void run(Integer institutionId, Integer appointmentId) {
        this.logger.debug("Notify patient of appointment {} in institution {}",appointmentId, institutionId);
        appointmentNotificationStorage.fetchNotifyPatientData(appointmentId)
                .ifPresent(sendAppointmentNotification::run);
    }
}
