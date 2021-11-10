package net.pladema.hsi.extensions.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import net.pladema.hsi.extensions.configuration.plugins.SystemMenuExtensionPlugin;
import net.pladema.hsi.extensions.domain.ExtensionService;
import net.pladema.hsi.extensions.infrastructure.repository.DefaultExtensionService;
import net.pladema.hsi.extensions.infrastructure.repository.DemoExtensionService;
import net.pladema.hsi.extensions.infrastructure.repository.RestExtensionService;
import net.pladema.hsi.extensions.infrastructure.repository.WrapperExtensionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;


@Configuration
@ComponentScan(basePackages = "net.pladema.hsi.extensions")
@PropertySource(value = "classpath:extensions.properties", ignoreResourceNotFound = true)
public class ExtensionsAutoConfiguration {

	@Bean
	public ExtensionService implExtensionService(
			ExtensionAuthorization extensionAuthorization,
			RestExtensionWsConfig wsConfig,
			List<SystemMenuExtensionPlugin> systemMenuExtensionPlugins
	) throws Exception {
		return new WrapperExtensionService(
				extensionAuthorization,
				fromConfig(wsConfig),
				systemMenuExtensionPlugins
		);
	}

	private static ExtensionService fromConfig(RestExtensionWsConfig wsConfig) throws Exception {
		if (wsConfig.getBaseUrl() == null || wsConfig.getBaseUrl().isBlank())
			return new DefaultExtensionService();
		if ("demo".equals(wsConfig.getBaseUrl()))
			return new DemoExtensionService();
		return new RestExtensionService(new RestTemplateSSL(new LoggingRequestInterceptor()), wsConfig);
	}
}
