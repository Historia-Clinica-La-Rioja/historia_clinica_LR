package net.pladema.medicalconsultation.appointment.service.notifypatient;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.AppointmentNotificationStorage;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.NotifyPatientBo;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.SendAppointmentNotification;

@Service
@Slf4j
public class NotifyPatient {

	private final AppointmentNotificationStorage appointmentNotificationStorage;

	private final SendAppointmentNotification sendAppointmentNotification;

	public NotifyPatient(AppointmentNotificationStorage appointmentNotificationStorage,
						 SendAppointmentNotification sendAppointmentNotification) {
		this.appointmentNotificationStorage = appointmentNotificationStorage;
		this.sendAppointmentNotification = sendAppointmentNotification;
	}

	public void run(Integer institutionId, Integer appointmentId) {
		log.debug("Notify patient of appointment {} in institution {}", appointmentId, institutionId);
		appointmentNotificationStorage.fetchNotifyPatientData(appointmentId)
				.filter(NotifyPatientBo::hasTopic)
				.ifPresent(sendAppointmentNotification::run);
	}


}
