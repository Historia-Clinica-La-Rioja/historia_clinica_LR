package ar.lamansys.sgx.shared.notifications.templating.engine.impl;

import org.springframework.context.MessageSource;
import org.thymeleaf.spring5.SpringTemplateEngine;

import ar.lamansys.sgx.shared.emails.domain.MailMessageBo;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.notifications.templating.INotificationTemplateEngine;
import ar.lamansys.sgx.shared.notifications.templating.NotificationTemplateInput;
import ar.lamansys.sgx.shared.notifications.templating.domain.NotificationEnv;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailTemplateEngine implements INotificationTemplateEngine<MailMessageBo> {
	private final TextTemplateEngine textTemplateEngine;
	private final HTMLTemplateEngine htmlTemplateEngine;

	public MailTemplateEngine(
			String domain,
			MessageSource messageSource,
			SpringTemplateEngine templateEngine
	) {

        this.textTemplateEngine = new TextTemplateEngine(
				() -> new NotificationEnv(domain),
				messageSource
		);
		this.htmlTemplateEngine = new HTMLTemplateEngine(
				() -> new NotificationEnv(domain),
				templateEngine
		);
	}

	@Override
	public MailMessageBo process(RecipientBo recipient, NotificationTemplateInput<?> message) throws TemplateException {
		return new MailMessageBo(
				processSubject(recipient, message),
				processBody(recipient, message),
				message.attachments
		);
	}

	private String processSubject(RecipientBo recipient, NotificationTemplateInput<?> message) throws TemplateException {
		return (message.subject!=null) ? message.subject : textTemplateEngine.process(recipient, message.withPrefixId("mail.subject."));
	}

	private String processBody(RecipientBo recipient, NotificationTemplateInput<?> message) throws TemplateException {
		return htmlTemplateEngine.process(recipient, message);
	}
}
