package ar.lamansys.sgx.auth.twoFactorAuthentication.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class SetTwoFactorAuthenticationBo {

	private String sharedSecretBarCode;

	private String sharedSecret;

}
