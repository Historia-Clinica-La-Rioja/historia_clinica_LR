package ar.lamansys.sgx.shared.notifications.templating;

import static ar.lamansys.sgx.shared.templating.utils.SpringTemplateUtils.createCustomizableHtmlTemplateEngine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.nfs.FileConfiguration;
import ar.lamansys.sgx.shared.notifications.templating.engine.impl.MailTemplateEngine;

@Configuration
public class NotificationTemplateConfig {
	@Bean
	public MailTemplateEngine mailTemplateEngine(
			@Value("${app.env.domain}") String domain,
			MessageSource messageSource,
			ApplicationContext applicationContext,
			FileConfiguration fileConfiguration
	) {
		var templateEngine = createCustomizableHtmlTemplateEngine(
				"mails",
				fileConfiguration,
				applicationContext
		);
		return new MailTemplateEngine(domain, messageSource, templateEngine);
	}
}
