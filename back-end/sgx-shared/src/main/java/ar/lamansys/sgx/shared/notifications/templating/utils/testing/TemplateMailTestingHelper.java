package ar.lamansys.sgx.shared.notifications.templating.utils.testing;

import static ar.lamansys.sgx.shared.notifications.templating.utils.testing.TemplateTestingUtils.GENERIC_RECIPIENT;
import static ar.lamansys.sgx.shared.notifications.templating.utils.testing.TemplateTestingUtils.createExpectedResultAsserter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import ar.lamansys.sgx.shared.emails.domain.MailMessageBo;
import ar.lamansys.sgx.shared.notifications.templating.engine.impl.MailTemplateEngine;
import ar.lamansys.sgx.shared.notifications.templating.NotificationTemplateInput;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TemplateMailTestingHelper<T> {
	private final MessageSource messageSource;
	protected ApplicationContext applicationContext;

    public MailMessageBo renderTemplate(Domain domain, String scenario, NotificationTemplateInput<T> notificationArgs) throws TemplateException {
		var mailTemplateEngine = new MailTemplateEngine(
				domain.value,
				messageSource,
				"classpath:/templates/mails/",
				applicationContext
		);
		var mail = mailTemplateEngine.process(GENERIC_RECIPIENT, notificationArgs);
		var mailBodyResultAsserter = createExpectedResultAsserter("mail-body", notificationArgs.templateId);
		mailBodyResultAsserter.accept(scenario, mail.body);
		return mail;
    }

	public MailMessageBo renderTemplate(String scenario, NotificationTemplateInput<T> notificationArgs) throws TemplateException {
		return renderTemplate(Domain.LOCALHOST, scenario, notificationArgs);
	}

	public enum Domain {
		LOCALHOST("localhost:5005"),
		PLADEMA("sgh.pladema.net"),
		;

		public final String value;

		Domain(String value) {
			this.value = value;
		}


	}

}
