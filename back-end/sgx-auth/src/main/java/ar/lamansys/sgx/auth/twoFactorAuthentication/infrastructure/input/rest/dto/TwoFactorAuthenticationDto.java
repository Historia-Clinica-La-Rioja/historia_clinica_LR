package ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class TwoFactorAuthenticationDto {

	private String sharedSecretBarCode;

	private String sharedSecret;

}
