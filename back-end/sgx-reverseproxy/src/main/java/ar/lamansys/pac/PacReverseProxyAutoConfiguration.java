package ar.lamansys.pac;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ar.lamansys.base.ReverseProxyAutoConfiguration;
import ar.lamansys.base.application.reverseproxyrest.RestReverseProxy;
import ar.lamansys.base.application.reverseproxyrest.configuration.RestUtils;
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
	private final RestReverseProxy restReverseProxy;

	@NonNull
	private String username;
	@NonNull
	private String password;

	public PacReverseProxyAutoConfiguration(ReverseProxyAutoConfiguration reverseProxyAutoConfiguration, RestReverseProxy restReverseProxy) {
		this.reverseProxyAutoConfiguration = reverseProxyAutoConfiguration;
		this.restReverseProxy = restReverseProxy;
	}

	@PostConstruct
	public void started() {
		String basicAuthorization = RestUtils.getBasicAuthenticationHeader(username, password);
		this.restReverseProxy.addHeaders(Map.of("Authorization", basicAuthorization));
		log.debug("PacReverseProxyAutoConfiguration -> Authorization {}", basicAuthorization);
	}
}
