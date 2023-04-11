package ar.lamansys.pac;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ar.lamansys.base.configuration.HttpClientConfiguration;
import ar.lamansys.base.configuration.RestUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.PostConstruct;

@Configuration
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ComponentScan(basePackages = {"ar.lamansys.pac"})
@ConfigurationProperties(prefix = "app.pac")
@EntityScan(basePackages = {"ar.lamansys.pac"})
@Slf4j
public class PacReverseProxyAutoConfiguration {

	@NonNull
	private String server;
	@NonNull
	private String proxy;
	@NonNull
	private String username;
	@NonNull
	private String password;

	private Map<String, String> headers = new HashMap<>();

	@Bean
	public HttpClientConfiguration configuration() {
		return new HttpClientConfiguration();
	}

	@PostConstruct
	public void setAuthorizationHeader() {
		headers.put("Authorization", RestUtils.getBasicAuthenticationHeader(username, password));
	}

}
