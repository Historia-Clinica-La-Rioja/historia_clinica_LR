package net.pladema.snowstorm.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ws.sisa")
@Getter
@Setter
public class SisaWSConfig extends WSConfig {

	private static final String BASE = "https://ws400-qa.sisa.msal.gov.ar";

	private static final String CASONOMINAL = "/snvsCasoNominal/v2/snvsCasoNominal";

	private static final long DEFAULT_TOKEN_EXPIRATION = -1L;

	private long tokenExpiration = DEFAULT_TOKEN_EXPIRATION;

	//Headers

	private String appId;

	private String appKey;

	public SisaWSConfig() {
		super(BASE, false);
	}

	public String getCasoNominal() {
		return CASONOMINAL;
	}
}
