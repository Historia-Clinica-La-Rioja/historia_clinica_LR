package ar.lamansys.sgx.shared.notifications.templating.engine.impl;

import java.util.function.Supplier;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import ar.lamansys.sgx.shared.notifications.templating.engine.NotificationTemplateEngine;
import ar.lamansys.sgx.shared.notifications.templating.domain.NotificationContext;
import ar.lamansys.sgx.shared.notifications.templating.domain.NotificationEnv;

public class HTMLTemplateEngine extends NotificationTemplateEngine<String> {
	private final SpringTemplateEngine springTemplateEngine;

	protected HTMLTemplateEngine(Supplier<NotificationEnv> environmentSupplier, SpringTemplateEngine springTemplateEngine) {
		super(environmentSupplier);
		this.springTemplateEngine = springTemplateEngine;
	}

	@Override
	protected String processImpl(String templateId, NotificationContext context) {
		return springTemplateEngine.process(
				templateId,
				new Context(context.locale, context.variables)
		);
	}
}
