package net.pladema.digitalsignature.infrastructure.output.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "ws.firmador")
@Getter
@Setter
public class FirmadorWSConfig extends WSConfig {

	private static final Duration DEFAULT_TOKEN_EXPIRATION = Duration.ofSeconds(10L);

	private final String apiKey;

	private final String apiToken;

	private final String pathLogin;

	private final String pathFirmador;

	private Duration tokenExpiration = DEFAULT_TOKEN_EXPIRATION;

	public FirmadorWSConfig(@Value("${ws.firmador.url.base:}") String baseUrl,
							 @Value("${ws.firmador.api.key:}") String apiKey,
							 @Value("${ws.firmador.api.token:}") String apiToken,
							 @Value("${ws.firmador.path.login:}") String pathLogin,
							@Value("${ws.firmador.path.multiple.sign:}") String pathFirmador) {
		super(baseUrl, false);
		this.apiKey = apiKey;
		this.apiToken = apiToken;
		this.pathLogin = pathLogin;
		this.pathFirmador = pathFirmador;
	}}