package ar.lamansys.base;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ar.lamansys.base.application.reverseproxyrest.configuration.HttpClientConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(value = "base")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "app.proxy")
@ComponentScan(basePackages = {"ar.lamansys.base"})
@EntityScan(basePackages = {"ar.lamansys.base"})
@Slf4j
public class ReverseProxyAutoConfiguration {

	@NonNull
	private String server;
	@NonNull
	private String proxy;
	private Map<String, String> headers = new HashMap<>();

	@Bean
	public HttpClientConfiguration configuration() {
		return new HttpClientConfiguration();
	}

	@Bean
	@ConditionalOnProperty(prefix = "app.filters", name = "enable-cors", havingValue = "true")
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NonNull CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "POST", "OPTIONS")
						.allowedHeaders("Origin", "Content-Type", "Authorization")
						.maxAge(3600L);
			}
		};
	}

}
