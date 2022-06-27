package ar.lamansys.sgx.shared.templating;

import java.nio.charset.StandardCharsets;

import org.springframework.context.ApplicationContext;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

public class SpringTemplateUtils {

	public static SpringTemplateEngine createJsonTemplateEngine(
			String templatePrefix,
			ApplicationContext applicationContext
	) {
		return springTemplateEngine(
				jsonTemplateResolver(
						templatePrefix,
						applicationContext
				)
		);
	}

	public static SpringTemplateEngine createHtmlTemplateEngine(
			String templatePrefix,
			ApplicationContext applicationContext
	) {
		return springTemplateEngine(
				htmlTemplateResolver(
						templatePrefix,
						applicationContext
				)
		);
	}

	public static SpringTemplateEngine springTemplateEngine(
			SpringResourceTemplateResolver chartsTemplateResolver
	) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(chartsTemplateResolver);
		templateEngine.addDialect(new Java8TimeDialect());
		return templateEngine;
	}

	private static SpringResourceTemplateResolver jsonTemplateResolver(
			String prefix,
			ApplicationContext applicationContext
	) {

		SpringResourceTemplateResolver jsonTemplateResolver = new SpringResourceTemplateResolver();
		jsonTemplateResolver.setPrefix(prefix);
		jsonTemplateResolver.setSuffix(".json");
		jsonTemplateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
		jsonTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		jsonTemplateResolver.setCheckExistence(true);
		jsonTemplateResolver.setCacheable(false);
		jsonTemplateResolver.setApplicationContext(applicationContext);

		return jsonTemplateResolver;
	}

	private static SpringResourceTemplateResolver htmlTemplateResolver(
			String prefix,
			ApplicationContext applicationContext
	) {
		SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
		emailTemplateResolver.setPrefix(prefix);
		emailTemplateResolver.setSuffix(".html");
		emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
		emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		emailTemplateResolver.setCheckExistence(true);
		emailTemplateResolver.setCacheable(false);
		emailTemplateResolver.setApplicationContext(applicationContext);
		return emailTemplateResolver;
	}
}
