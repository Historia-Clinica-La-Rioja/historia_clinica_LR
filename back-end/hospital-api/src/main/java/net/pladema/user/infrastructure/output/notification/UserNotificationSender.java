package net.pladema.user.infrastructure.output.notification;

import ar.lamansys.sgx.shared.notifications.application.NotificationSender;
import ar.lamansys.sgx.shared.notifications.application.RecipientMapper;
import ar.lamansys.sgx.shared.notifications.application.RecipientNotificationSender;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * Implementa el envío de mensajes al usuario
 */
@Slf4j
@Service
public class UserNotificationSender extends RecipientNotificationSender<UserRecipient> {
	public UserNotificationSender(
			RecipientMapper<UserRecipient> recipientMapper,
			NotificationSender notificationSender
	) {
		super(recipientMapper, notificationSender);
	}
}
