package ar.lamansys.sgx.shared.notifications.application;

import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.templating.INotificationTemplateEngine;
import ar.lamansys.sgx.shared.templating.NotificationTemplateInput;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public abstract class NotificationChannelManager<U> {
	private final NotificationChannel<U> channel;
	private final INotificationTemplateEngine<U> templateEngine;

	public abstract boolean accept(RecipientBo recipientBo);

	public void send(RecipientBo recipient, NotificationTemplateInput<?> message) {
		try {
			var renderedMessage = templateEngine.process(recipient, message);
			channel.send(recipient, renderedMessage);
		} catch (TemplateException e) {
			log.error("Notification sent to <{}>: {}", recipient, e.getMessage(), e);
		}
	}
}
