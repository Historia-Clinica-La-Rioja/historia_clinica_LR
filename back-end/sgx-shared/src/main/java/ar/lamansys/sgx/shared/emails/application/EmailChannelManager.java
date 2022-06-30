package ar.lamansys.sgx.shared.emails.application;


import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.notifications.application.NotificationChannelManager;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.templating.MailTemplateEngine;

@Service
public class EmailChannelManager extends NotificationChannelManager {

	public EmailChannelManager(
			EmailNotificationChannel emailChannel,
			MailTemplateEngine mailTemplateEngine
	) {
		super(emailChannel, mailTemplateEngine);
	}

	@Override
	public boolean accept(RecipientBo recipientBo) {
		return recipientBo.email != null;
	}

}
