package ar.lamansys.sgx.shared.notifications.application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.templating.NotificationTemplateInput;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class NotificationSender {
	private final ExecutorService executorService = Executors.newCachedThreadPool();
	private final List<NotificationChannelManager<?>> channelManagers;

	public void send(RecipientBo recipient, NotificationTemplateInput<?> message) {
		log.debug("Sending '{}' notification to {}", message.templateId, recipient);

		var channelSender = channelManagers.stream()
				.filter(channelManager -> channelManager.accept(recipient))
				.findFirst();

		if (channelSender.isEmpty()) {
			log.debug("Can't find notification channel for {}", recipient);
			return;
		}

		executorService.execute(new NotificationTask(
				channelSender.get(),
				recipient,
				message
		));

	}

}
