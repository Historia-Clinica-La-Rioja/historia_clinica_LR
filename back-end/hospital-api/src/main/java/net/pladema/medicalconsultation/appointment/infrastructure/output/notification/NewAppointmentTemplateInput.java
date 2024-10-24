package net.pladema.medicalconsultation.appointment.infrastructure.output.notification;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.notifications.templating.NotificationTemplateInput;

/**
 * Define el nombre del template y los argumentos que se requieren como input para renderizar el mensaje.
 */
public class NewAppointmentTemplateInput extends NotificationTemplateInput<NewAppointmentNotificationArgs> {
	public static final String TEMPLATE_ID = "new-appointment";

	public NewAppointmentTemplateInput(NewAppointmentNotificationArgs args) {
		super(TEMPLATE_ID, args,AppFeature.HABILITAR_NOTIFICACIONES_TURNOS);
	}
}