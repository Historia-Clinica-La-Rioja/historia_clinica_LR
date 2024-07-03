package ar.lamansys.online.infraestructure.notification.message;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.notifications.templating.NotificationTemplateInput;

public class ConfirmarReservaTemplateInput extends NotificationTemplateInput<ConfirmarReservaNotificationArgs> {
	public static final String TEMPLATE_ID = "email-confirmar-reserva";
	public ConfirmarReservaTemplateInput(ConfirmarReservaNotificationArgs args) {
		super(TEMPLATE_ID, args, AppFeature.HABILITAR_MAIL_RESERVA_TURNO);
	}
}
