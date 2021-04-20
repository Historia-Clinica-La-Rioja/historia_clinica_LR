package net.pladema.hsi.extensions.configuration;

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
	private String mode;
	@Bean
	public ExtensionService implExtensionService() throws Exception {
		if ("demo".equals(mode))
			return new DemoExtensionService();
		return new DefaultExtensionService();
	}
}
