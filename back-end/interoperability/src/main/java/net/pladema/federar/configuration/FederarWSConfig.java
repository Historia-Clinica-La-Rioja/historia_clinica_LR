package net.pladema.federar.configuration;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import lombok.Setter;

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
	private Duration tokenExpiration;

	private Map<String, String> claims;
	private Map<String, String> auth;
	private static final String AUTHENTICATION = "/bus-auth/auth";
	private static final String AUTHORIZATION = "/bus-auth/tokeninfo";

	private static final long DEFAULT_REQUEST_TIME_OUT = 5000L;

	private long requestTimeOut = DEFAULT_REQUEST_TIME_OUT;


	public FederarWSConfig(@Value("${ws.federar.url.base}") String baseUrl,
						   @Value("${ws.federar.token.expiration}") Duration tokenExpiration) {
		super(baseUrl, false);
		this.tokenExpiration = tokenExpiration;
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

	public Long getRequestTimeOut() {
		return requestTimeOut;
	}
}
