package net.pladema.renaper.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;

@Component
@ConfigurationProperties(prefix="ws.renaper")
@Getter
@Setter
public class RenaperWSConfig extends WSConfig{

    private static final String PERSONA = "persona";
	private static final String COBERTURA = "cobertura";
	private static final long DEFAULT_TOKEN_EXPIRATION = 10L;
	
	private String nombre;
    private String clave;
    private String dominio;
    private Map<String, String> url;
	private long tokenExpiration = DEFAULT_TOKEN_EXPIRATION;

	
	public RenaperWSConfig(@Value("${ws.renaper.url.base}") String baseUrl) {
		super(baseUrl);
		url = new HashMap<>();
	}

	public String getUrlCobertura() {
		return url.get(COBERTURA);
	}
	
	public void setUrlCobertura(String urlCobertura) {
		url.put(COBERTURA, urlCobertura);
	}
	
	public String getUrlPersona() {
		return url.get(PERSONA);
	}
	
	public void setUrlPersona(String urlPersona) {
		url.put(PERSONA, urlPersona);
	}
	
	
}
