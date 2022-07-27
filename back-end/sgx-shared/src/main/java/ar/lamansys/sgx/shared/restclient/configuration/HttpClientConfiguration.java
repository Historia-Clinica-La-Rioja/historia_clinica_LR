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
	private String proxyHost;
	private Integer proxyPort;
	private Integer timeout;
	private boolean trustInvalidCertificate;

	public HttpClientConfiguration with(boolean newTrustInvalidCertificate) {
		return new HttpClientConfiguration(
				proxyHost,
				proxyPort,
				timeout,
				newTrustInvalidCertificate
		);
	}

	public HttpClientConfiguration with(Number newTimeout) {
		return new HttpClientConfiguration(
				proxyHost,
				proxyPort,
				newTimeout.intValue(),
				trustInvalidCertificate
		);
	}
}
