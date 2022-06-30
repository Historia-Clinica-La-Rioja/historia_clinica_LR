package ar.lamansys.sgx.shared.templating;

import static ar.lamansys.sgx.shared.templating.SpringTemplateUtils.createHtmlTemplateEngine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.emails.domain.MailMessageBo;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.templating.domain.NotificationEnv;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailTemplateEngine implements INotificationTemplateEngine<MailMessageBo> {
	private final TextTemplateEngine textTemplateEngine;
	private final HTMLTemplateEngine htmlTemplateEngine;

	public MailTemplateEngine(
			@Value("${app.env.domain}") String domain,
			MessageSource messageSource,
			@Value("${app.mail.templates:classpath:/templates/mails/}") String templatePrefix,
			ApplicationContext applicationContext
	) {

        this.textTemplateEngine = new TextTemplateEngine(
				() -> new NotificationEnv(domain),
				messageSource
		);
		this.htmlTemplateEngine = new HTMLTemplateEngine(
				() -> new NotificationEnv(domain),
				createHtmlTemplateEngine(
					templatePrefix,
					applicationContext
				));
	}

	@Override
	public MailMessageBo process(RecipientBo recipient, NotificationTemplateInput<?> message) throws TemplateException {
		String subject = textTemplateEngine.process(recipient, message.withPrefixId("mail.subject."));
		String html = htmlTemplateEngine.process(recipient, message);
		return new MailMessageBo(subject, html);
	}
}
