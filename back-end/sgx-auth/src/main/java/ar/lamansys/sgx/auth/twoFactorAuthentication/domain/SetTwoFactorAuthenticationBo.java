package ar.lamansys.sgx.auth.twoFactorAuthentication.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
public class SetTwoFactorAuthenticationBo {

	private String account;

	private String issuer;

	private String sharedSecret;

	@Override
	public String toString() {
		return "SetTwoFactorAuthenticationBo{" + "account='" + account + '\'' + ", issuer='" + issuer + '\'' + ", sharedSecret=*******}";
	}
}
