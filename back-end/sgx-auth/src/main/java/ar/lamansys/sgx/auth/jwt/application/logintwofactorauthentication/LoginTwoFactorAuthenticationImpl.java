package ar.lamansys.sgx.auth.jwt.application.logintwofactorauthentication;

import ar.lamansys.sgx.auth.jwt.application.generatetoken.GenerateToken;
import ar.lamansys.sgx.auth.jwt.application.logintwofactorauthentication.exceptions.BadOTPException;
import ar.lamansys.sgx.auth.jwt.application.logintwofactorauthentication.exceptions.BadOTPExceptionEnum;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;

import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoStorage;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginTwoFactorAuthenticationImpl implements LoginTwoFactorAuthentication {

	private final TwoFactorAuthenticationStorage twoFactorAuthenticationStorage;
	private final UserInfoStorage userInfoStorage;
	private final GenerateToken generateToken;

	@Override
	public JWTokenBo execute(String code) {
		log.debug("Input parameter -> code {}", code);
		Integer userId = UserInfo.getCurrentAuditor();
		UserInfoBo userInfo = userInfoStorage.getUser(userId);

		if (!this.twoFactorAuthenticationStorage.verifyCode(code)) {
			throw new BadOTPException(BadOTPExceptionEnum.INVALID_CODE, "Código de verificación inválido");
		}

		JWTokenBo result = generateToken.generateTokens(userInfo.getId(), userInfo.getUsername());
		userInfoStorage.updateLoginDate(userInfo.getUsername());
		return result;
	}
}
