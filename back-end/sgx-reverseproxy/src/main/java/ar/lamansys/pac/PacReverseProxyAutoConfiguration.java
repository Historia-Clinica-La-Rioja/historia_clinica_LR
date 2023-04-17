package ar.lamansys.pac;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import ar.lamansys.base.ReverseProxyAutoConfiguration;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration(value = "pac")
@ConditionalOnProperty(prefix = "app.proxy", name = "type", havingValue = "pac")
@Getter
@Setter
@ConfigurationProperties(prefix = "app.proxy.pac")
@ComponentScan(basePackages = {"ar.lamansys.pac"})
@EntityScan(basePackages = {"ar.lamansys.pac"})
@Slf4j
public class PacReverseProxyAutoConfiguration {

	private final ReverseProxyAutoConfiguration reverseProxyAutoConfiguration;
	private final HttpHeaders defaultHeaders;

	@NonNull
	private String username;
	@NonNull
	private String password;

	public PacReverseProxyAutoConfiguration(ReverseProxyAutoConfiguration reverseProxyAutoConfiguration, HttpHeaders defaultHeaders) {
		this.reverseProxyAutoConfiguration = reverseProxyAutoConfiguration;
		this.defaultHeaders = defaultHeaders;
	}

	@PostConstruct
	public void started() {
		defaultHeaders.setBasicAuth(username, password);
		log.debug("PacReverseProxyAutoConfiguration -> Authorization {}", defaultHeaders.get(HttpHeaders.AUTHORIZATION));
	}
}
