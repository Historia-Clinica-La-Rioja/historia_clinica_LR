package net.pladema.federar.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import net.pladema.federar.services.FederarUtils;
import net.pladema.sgx.restclient.configuration.WSConfig;

@Component
@ConfigurationProperties(prefix="ws.federar")
@Getter
@Setter
public class FederarWSConfig extends WSConfig{

	private static final long DEFAULT_TOKEN_EXPIRATION = 180l;
	private static final String FEDERATE ="federate";
	
    private Map<String, String> url;
    private Map<String, String> claims;
    private Map<String, String> auth;
    
	private long tokenExpiration = DEFAULT_TOKEN_EXPIRATION;

	
	public FederarWSConfig(@Value("${ws.federar.url.base:https://testapp.hospitalitaliano.org.ar}") String baseUrl) {
		super(baseUrl);
		url = new HashMap<>();
	}
	
	public String getIss() {
		return claims.get(FederarUtils.getIss());
	}
	
	public String getDomain() {
		return claims.get(FederarUtils.getIss());
	}
	
	public String getSub() {
		return claims.get(FederarUtils.getSub());
	}
	
	public String getAud() {
		return claims.get(FederarUtils.getAud());
	}
		
	public String getName() {
		return claims.get(FederarUtils.getName());
	}
	
	public String getIdent() {
		return claims.get(FederarUtils.getIdent());
	}
	
	public String getRole() {
		return claims.get(FederarUtils.getRole());
	}
	
	public String getGrantType() {
		return auth.get(FederarUtils.getGrantType());
	}
	
	public String getSignKey() {
		return auth.get(FederarUtils.getSignKey());
	}
	
	public String getScope() {
		return auth.get(FederarUtils.getScope());
	}

	public String getClientAssertionType() {
		return auth.get(FederarUtils.getClientAssertionType());
	}
	
	public String getTokenValidationURL() {
		return url.get(FederarUtils.getTokenValid());
	}
	
	public String getLocalSearchIdUrl() {
		return url.get(FederarUtils.getSearchLocalId());
	}
	
	public String getFederateUrl() {
		return url.get(FEDERATE);
	}
	
}
