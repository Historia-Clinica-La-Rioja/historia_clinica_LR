package net.pladema.hsi.extensions.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import net.pladema.hsi.extensions.infrastructure.repository.RestExtensionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hsi.extensions.domain.ExtensionService;
import net.pladema.hsi.extensions.infrastructure.repository.DefaultExtensionService;
import net.pladema.hsi.extensions.infrastructure.repository.DemoExtensionService;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "hsi.extensions")
@ComponentScan(basePackages = "net.pladema.hsi.extensions")
@PropertySource(value = "classpath:extensions.properties", ignoreResourceNotFound = true)
public class ExtensionsAutoConfiguration {

	@Value("hsi.extensions.rest.url")
	private String url;

	@Bean
	public ExtensionService implExtensionService() throws Exception {
		if (url == null || url.isBlank())
			return new DefaultExtensionService();
		if ("demo".equals(url))
			return new DemoExtensionService();
		return new RestExtensionService(new RestTemplateSSL(new LoggingRequestInterceptor()), new RestExtensionWsConfig(url));
	}
}
