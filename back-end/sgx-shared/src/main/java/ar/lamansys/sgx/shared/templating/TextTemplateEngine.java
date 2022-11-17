package ar.lamansys.sgx.shared.templating;

import java.util.function.Supplier;

import org.springframework.context.MessageSource;

import ar.lamansys.sgx.shared.templating.domain.NotificationContext;
import ar.lamansys.sgx.shared.templating.domain.NotificationEnv;
import ar.lamansys.sgx.shared.templating.impl.NotificationTemplateEngine;
import lombok.AllArgsConstructor;


public class TextTemplateEngine extends NotificationTemplateEngine<String> {
	private final MessageSource messageSource;

	public TextTemplateEngine(Supplier<NotificationEnv> environmentSupplier, MessageSource messageSource) {
		super(environmentSupplier);
		this.messageSource = messageSource;
	}

	@Override
	protected String processImpl(String templateId, NotificationContext context) {
		return messageSource.getMessage(templateId, null, context.locale);
	}
}
