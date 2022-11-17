package ar.lamansys.sgx.shared.notifications.application;

import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.templating.NotificationTemplateInput;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class NotificationTask implements Runnable {
	private final NotificationChannelManager<?> channelSender;
	private final RecipientBo recipient;
	private final NotificationTemplateInput<?> templateInput;

	@Override
	public void run() {
		channelSender.send(recipient, templateInput);
	}

}
