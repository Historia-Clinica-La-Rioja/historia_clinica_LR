package ar.lamansys.sgx.shared.notifications.templating.engine.impl;

import java.util.function.Supplier;

import org.springframework.context.MessageSource;

import ar.lamansys.sgx.shared.notifications.templating.engine.NotificationTemplateEngine;
import ar.lamansys.sgx.shared.notifications.templating.domain.NotificationContext;
import ar.lamansys.sgx.shared.notifications.templating.domain.NotificationEnv;


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
