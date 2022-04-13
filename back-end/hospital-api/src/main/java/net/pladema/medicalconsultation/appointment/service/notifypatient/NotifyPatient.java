package net.pladema.medicalconsultation.appointment.service.notifypatient;

import net.pladema.events.EHospitalApiTopicDto;
import net.pladema.events.HospitalApiPublisher;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.AppointmentNotificationStorage;
import net.pladema.medicalconsultation.appointment.service.domain.notifypatient.SendAppointmentNotification;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.NotifyPatientBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotifyPatient {

	private final AppointmentNotificationStorage appointmentNotificationStorage;
	private final SendAppointmentNotification sendAppointmentNotification;
	private final Logger logger;
	private final HospitalApiPublisher hospitalApiPublisher;

	public NotifyPatient(AppointmentNotificationStorage appointmentNotificationStorage, SendAppointmentNotification sendAppointmentNotification, HospitalApiPublisher hospitalApiPublisher) {
		this.appointmentNotificationStorage = appointmentNotificationStorage;
		this.sendAppointmentNotification = sendAppointmentNotification;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.hospitalApiPublisher = hospitalApiPublisher;
	}

	public void run(Integer institutionId, Integer appointmentId) {
		this.logger.debug("Notify patient of appointment {} in institution {}", appointmentId, institutionId);
		appointmentNotificationStorage.fetchNotifyPatientData(appointmentId).ifPresent(a -> {
					NotifyPatientBo b = new NotifyPatientBo(a.getAppointmentId(), a.getPatientName(), a.getSectorId(), a.getDoctorName(), a.getDoctorsOfficeName(), a.getTopic());
					hospitalApiPublisher.publishLlamador(b);
				}
		);
	}

}
