package ar.lamansys.sgx.shared.notifications.application;

import ar.lamansys.sgx.shared.templating.NotificationTemplateInput;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecipientNotificationSender<T> {
	private final RecipientMapper<T> recipientMapper;
	private final NotificationSender notificationSender;

	public void send(T recipientId, NotificationTemplateInput<?> message) {
		recipientMapper.toRecipient(recipientId)
				.ifPresent(recipient -> notificationSender.send(recipient, message));
	}
}
