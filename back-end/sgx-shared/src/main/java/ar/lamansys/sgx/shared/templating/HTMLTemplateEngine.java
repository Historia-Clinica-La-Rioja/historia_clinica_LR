package ar.lamansys.sgx.shared.templating;

import java.util.function.Supplier;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import ar.lamansys.sgx.shared.templating.domain.NotificationContext;
import ar.lamansys.sgx.shared.templating.domain.NotificationEnv;
import ar.lamansys.sgx.shared.templating.impl.NotificationTemplateEngine;

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
