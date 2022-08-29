package ar.lamansys.sgx.shared.restclient.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "app.http.client")
public class HttpClientConfiguration {
	private String proxy;
	private Integer timeout;
	private boolean trustInvalidCertificate;

	public HttpClientConfiguration with(boolean newTrustInvalidCertificate) {
		return new HttpClientConfiguration(
				proxy,
				timeout,
				newTrustInvalidCertificate
		);
	}

	public HttpClientConfiguration withTimeout(Number newTimeout) {
		return new HttpClientConfiguration(
				proxy,
				newTimeout.intValue(),
				trustInvalidCertificate
		);
	}

	public HttpClientConfiguration withProxy(String newProxy) {
		return new HttpClientConfiguration(
				newProxy,
				timeout,
				trustInvalidCertificate
		);
	}
}
