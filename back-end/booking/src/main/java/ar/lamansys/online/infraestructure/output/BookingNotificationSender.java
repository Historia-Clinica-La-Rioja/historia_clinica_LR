package ar.lamansys.online.infraestructure.output;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.notifications.application.NotificationSender;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.notifications.templating.NotificationTemplateInput;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class BookingNotificationSender {
	private final NotificationSender notificationSender;

	public void send(RecipientBo recipient, NotificationTemplateInput<?> message) {
		notificationSender.send(recipient, message);
	}
}
