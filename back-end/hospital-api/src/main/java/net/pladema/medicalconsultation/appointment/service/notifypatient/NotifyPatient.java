package net.pladema.medicalconsultation.appointment.service.notifypatient;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.AppointmentNotificationStorage;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.NotifyPatientBo;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.SendAppointmentNotification;
import net.pladema.medicalconsultation.appointment.service.exceptions.NotifyPatientException;

@AllArgsConstructor
@Slf4j
@Service
public class NotifyPatient {

	private final AppointmentNotificationStorage appointmentNotificationStorage;

	private final SendAppointmentNotification sendAppointmentNotification;


	public void run(Integer institutionId, Integer appointmentId) throws NotifyPatientException {
		log.debug("Notify patient of appointment {} in institution {}", appointmentId, institutionId);
		var notifyData = getNotifyData(appointmentId);

		assertTopicIsSet(notifyData);

		sendAppointmentNotification.run(notifyData, institutionId);
	}

	private static void assertTopicIsSet(NotifyPatientBo notifyData) throws NotifyPatientException {
		if (!notifyData.hasTVMonitor()) {
			throw  new NotifyPatientException(notifyData.getSectorId());
		}
	}

	private NotifyPatientBo getNotifyData(Integer appointmentId) throws NotifyPatientException {
		return appointmentNotificationStorage.fetchNotifyPatientData(appointmentId).orElseThrow(() -> new NotifyPatientException(null));
	}


}
