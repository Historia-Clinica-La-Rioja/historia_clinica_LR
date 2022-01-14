package ar.lamansys.sgx.auth.oauth.infrastructure.output.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthRefreshTokenPayload {

    private String clientId;

    private String grantType;

    private String refreshToken;

}
