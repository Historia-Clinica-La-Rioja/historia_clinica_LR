package ar.lamansys.sgx.shared.templating.utils.testing;

import static ar.lamansys.sgx.shared.templating.utils.testing.TemplateTestingUtils.GENERIC_RECIPIENT;
import static ar.lamansys.sgx.shared.templating.utils.testing.TemplateTestingUtils.createExpectedResultAsserter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import ar.lamansys.sgx.shared.emails.domain.MailMessageBo;
import ar.lamansys.sgx.shared.templating.MailTemplateEngine;
import ar.lamansys.sgx.shared.templating.NotificationTemplateInput;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TemplateMailTestingHelper<T> {
	private final MessageSource messageSource;
	protected ApplicationContext applicationContext;

    public MailMessageBo renderTemplate(String scenario, NotificationTemplateInput<T> notificationArgs) throws TemplateException {
		var mailTemplateEngine = new MailTemplateEngine(
				"localhost:5005",
				messageSource,
				"classpath:/templates/mails/",
				applicationContext
		);
		var mail = mailTemplateEngine.process(GENERIC_RECIPIENT, notificationArgs);
		var mailBodyResultAsserter = createExpectedResultAsserter("mail-body", notificationArgs.templateId);
		mailBodyResultAsserter.accept(scenario, mail.body);
		return mail;
    }

}
