package net.pladema.federar.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix="ws.federar")
@Conditional(FederarCondition.class)
@Getter
@Setter
public class FederarWSConfig extends WSConfig {

	private static final String ISS = "iss";
	private static final String SUB = "sub";
	private static final String AUD = "aud";
	private static final String NAME = "name";
	private static final String ROLE = "role";
	private static final String IDENT = "ident";
	private static final String GRANT_TYPE="client_credentials";
	private static final String SCOPE="Patient/*.read,Patient/*.write";
	private static final String CLIENT_ASSERTION_TYPE="urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
	private static final String SIGN_KEY = "signKey";

	//URL
	private static final String TOKEN_VALID="validateToken";
	private static final String PATIENT_SERVICE ="/masterfile-federacion-service/fhir/Patient";

	private static final long DEFAULT_TOKEN_EXPIRATION = 180L;

	private Map<String, String> claims;
	private Map<String, String> auth;

	private long tokenExpiration = DEFAULT_TOKEN_EXPIRATION;

	private static final String AUTHENTICATION = "/bus-auth/auth";
	private static final String AUTHORIZATION = "/bus-auth/tokeninfo";


	public FederarWSConfig(@Value("${ws.federar.url.base}") String baseUrl) {
		super(baseUrl, false);
	}

	public String getIss() {
		return claims.get(ISS);
	}

	public String getDomain() {
		return claims.get(ISS);
	}

	public String getSub() {
		return claims.get(SUB);
	}

	public String getAud() {
		return claims.get(AUD);
	}

	public String getName() {
		return claims.get(NAME);
	}

	public String getIdent() {
		return claims.get(IDENT);
	}

	public String getRole() {
		return claims.get(ROLE);
	}

	public String getGrantType() {
		return GRANT_TYPE;
	}

	public String getSignKey() {
		return auth.get(SIGN_KEY);
	}

	public String getScope() {
		return SCOPE;
	}

	public String getClientAssertionType() {
		return CLIENT_ASSERTION_TYPE;
	}

	public String getAuthenticationPath(){
		return AUTHENTICATION;
	}

	public String getAuthorizationPath() {
		return AUTHORIZATION;
	}

	public String getPatientService() {
		return PATIENT_SERVICE;
	}
}
