package net.pladema.person.infraestructure.output.notification;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.notifications.application.NotificationSender;
import ar.lamansys.sgx.shared.notifications.application.RecipientMapper;
import ar.lamansys.sgx.shared.notifications.application.RecipientNotificationSender;

/**
 * Implementa el envío de mensajes a la persona
 */
@Service
public class PersonNotificationSender extends RecipientNotificationSender<PersonRecipient> {
	public PersonNotificationSender(
			RecipientMapper<PersonRecipient> recipientMapper,
			NotificationSender notificationSender
	) {
		super(recipientMapper, notificationSender);
	}

}
