package ar.lamansys.base.application.reverseproxyrest.configuration;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Slf4j
@ConfigurationProperties(prefix = "app.http.client")
public class HttpClientConfiguration {
	private String proxy;
	private Integer timeout;
	private boolean trustInvalidCertificate;

	@SuppressWarnings("unused")
	public HttpClientConfiguration with(boolean newTrustInvalidCertificate) {
		return new HttpClientConfiguration(proxy, timeout, newTrustInvalidCertificate);
	}
	@SuppressWarnings("unused")
	public HttpClientConfiguration withTimeout(Number newTimeout) {
		return new HttpClientConfiguration(proxy, newTimeout.intValue(), trustInvalidCertificate);
	}
	@SuppressWarnings("unused")
	public HttpClientConfiguration withProxy(String newProxy) {
		return new HttpClientConfiguration(newProxy, timeout, trustInvalidCertificate);
	}

	@PostConstruct
	void started() {
		log.debug("default timeout defined {} seconds", timeout/1000);
	}
}
