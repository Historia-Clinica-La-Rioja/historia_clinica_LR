package net.pladema.renaper.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ws.renaper")
@Conditional(RenaperCondition.class)
@Getter
@Setter
public class RenaperWSConfig extends WSConfig{

    private static final String PERSONA = "/personas/renaper";
	private static final String COBERTURA = "/personas/cobertura";
	private static final String LOGIN = "/usuarios/aplicacion/login";
	private static final String BASE = "https://federador.msal.gob.ar/masterfile-federacion-service/api";

	private static final long DEFAULT_TOKEN_EXPIRATION = 10L;
	
	private String nombre;
    private String clave;
    private String dominio = "DOMINIOSINAUTORIZACIONDEALTA";
	private long tokenExpiration = DEFAULT_TOKEN_EXPIRATION;

	
	public RenaperWSConfig() {
		super(BASE, false);
	}

	public String getUrlCobertura() {
		return COBERTURA;
	}
	
	public String getUrlPersona() {
		return PERSONA;
	}

	public String getLoginPath(){
		return LOGIN;
	}
}
