package ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.configuration;

import ar.lamansys.sgx.shared.restclient.services.domain.LoginResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuthLoginResponse implements LoginResponse {

	private String accessToken;

	private Integer expiresIn;

	private Integer refreshExpiresIn;

	private String refreshToken;

	private String tokenType;

	private Integer notBeforePolicy;

	private String sessionState;

	private String scope;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public OAuthLoginResponse(@JsonProperty("access_token") String accessToken,
							  @JsonProperty("expires_in") Integer expiresIn,
							  @JsonProperty("refresh_expires_in")Integer refreshExpiresIn,
							  @JsonProperty("refresh_token")String refreshToken,
							  @JsonProperty("token_type") String tokenType,
							  @JsonProperty("not-before-policy") Integer notBeforePolicy,
							  @JsonProperty("session_state") String sessionState,
							  @JsonProperty("scope") String scope) {
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.refreshExpiresIn = refreshExpiresIn;
		this.refreshToken = refreshToken;
		this.tokenType = tokenType;
		this.notBeforePolicy = notBeforePolicy;
		this.sessionState = sessionState;
		this.scope = scope;
	}
	
	public String getToken() {
		return accessToken;
	}

	
}
