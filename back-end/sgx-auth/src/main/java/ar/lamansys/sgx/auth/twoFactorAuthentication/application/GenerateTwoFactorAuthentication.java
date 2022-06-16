package ar.lamansys.sgx.auth.twoFactorAuthentication.application;

import ar.lamansys.sgx.auth.twoFactorAuthentication.domain.SetTwoFactorAuthenticationBo;

public interface GenerateTwoFactorAuthentication {

	SetTwoFactorAuthenticationBo run();

}
