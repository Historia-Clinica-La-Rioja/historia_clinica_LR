package net.pladema.medicalconsultation.appointment.infraestructure.output.notification;
import net.pladema.medicalconsultation.appointment.infraestructure.output.notification.exceptions.NewAppointmentNotificationEnumException;
import net.pladema.medicalconsultation.appointment.infraestructure.output.notification.exceptions.NewAppointmentNotificationException;
import net.pladema.medicalconsultation.appointment.service.booking.BookingPersonService;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.application.port.NewAppointmentNotification;
import net.pladema.medicalconsultation.appointment.domain.NewAppointmentNotificationBo;
import net.pladema.patient.infraestructure.output.notification.PatientNotificationSender;
import net.pladema.patient.infraestructure.output.notification.PatientRecipient;

@AllArgsConstructor
@Slf4j
@Service
public class NewAppointmentNotificationImpl implements NewAppointmentNotification {
	private final PatientNotificationSender patientNotificationSender;
	private final BookingPersonService bookingPersonService;

	@Override
	public void run(NewAppointmentNotificationBo newAppointmentNotification) {
		String professionalName = bookingPersonService.getProfessionalName(newAppointmentNotification.diaryId)
				.orElseThrow(()-> new NewAppointmentNotificationException(NewAppointmentNotificationEnumException.PROFESSIONAL_NAME_NOT_FOUND, String.format("No se encontro el profesional de la agenda con id ",newAppointmentNotification.diaryId)));
		var notificationArgs = NewAppointmentNotificationArgs.builder();
		// se resuelven los argumentos que requiere el mensaje a enviar a partir del BO
		notificationArgs
				.professionalFullName(professionalName)
				.address(String.format("Dirección de Institución de Agenda #%s", newAppointmentNotification.diaryId))
				.day(String.format("%s", newAppointmentNotification.dateTypeId))
				.time(String.format("%s", newAppointmentNotification.hour))
				.institution(String.format("Institución de Agenda #%s", newAppointmentNotification.diaryId))
				.recomendation("...")
				.specialty("");
		this.patientNotificationSender.send(
				new PatientRecipient(newAppointmentNotification.patientId),
				new NewAppointmentTemplateInput(
						notificationArgs.build()
				)
		);
	}

}
