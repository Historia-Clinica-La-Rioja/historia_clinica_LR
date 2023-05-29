package ar.lamansys.base;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ar.lamansys.base.application.reverseproxyrest.configuration.HttpClientConfiguration;
import ar.lamansys.base.application.reverseproxyrest.configuration.RestTemplateSSL;
import ar.lamansys.base.application.reverseproxyrest.handler.RestResponseErrorHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Configuration(value = "base")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "app.proxy")
@ComponentScan(basePackages = {"ar.lamansys.base"})
@EntityScan(basePackages = {"ar.lamansys.base"})
public class ReverseProxyAutoConfiguration {

	@NonNull
	private String server;

	@Bean
	public RestTemplate restTemplate(HttpClientConfiguration configuration) throws Exception {
		RestTemplate restTemplate = new RestTemplateSSL(configuration);
		restTemplate.setErrorHandler(new RestResponseErrorHandler());
		return restTemplate;
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

	@Bean
	public HttpHeaders defaultHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_JSON);
		return headers;
	}

}
