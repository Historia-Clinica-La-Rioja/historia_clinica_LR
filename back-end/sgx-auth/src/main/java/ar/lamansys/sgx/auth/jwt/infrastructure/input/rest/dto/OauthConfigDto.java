package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OauthConfigDto {

	private String issuerUrl;

	private String clientId;

	private String logoutUrl;

	private Boolean enabled;

}
