package ar.lamansys.sgx.shared.templating;

import static ar.lamansys.sgx.shared.templating.utils.SpringTemplateUtils.createHtmlTemplateEngine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class CustomizableTemplateEngine {
	private final SpringTemplateEngine templateEngine;

	public CustomizableTemplateEngine(@Value("${app.customizable.templates}") String templatePrefix, ApplicationContext applicationContext) {
		this.templateEngine = createHtmlTemplateEngine(templatePrefix, applicationContext);
	}

	public final String process(String templateId, Context context) {
		return templateEngine.process(templateId, context);
	}

}
