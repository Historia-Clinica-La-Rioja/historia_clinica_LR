package ar.lamansys.sgx.auth.jwt.application.login;

import ar.lamansys.sgx.auth.jwt.application.generatepartiallyauthtoken.GeneratePartiallyAuthenticationToken;
import ar.lamansys.sgx.auth.user.application.fetchuserhastwofactorauthenticationenabled.FetchUserHasTwoFactorAuthenticationEnabled;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.auth.jwt.application.generatetoken.GenerateToken;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginEnumException;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.domain.LoginBo;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoStorage;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordEncryptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class LoginJWTImpl implements Login {

	private final UserInfoStorage userInfoStorage;

	private final PasswordEncryptor passwordEncryptor;

	private final GenerateToken generateToken;

	private final FeatureFlagsService featureFlagsService;

	private final GeneratePartiallyAuthenticationToken generatePartiallyAuthenticationToken;

	private final FetchUserHasTwoFactorAuthenticationEnabled fetchUserHasTwoFactorAuthenticationEnabled;

	@Override
	public JWTokenBo execute(LoginBo login) throws BadLoginException {
		UserInfoBo user = userInfoStorage.getUser(login.username);
		if (user == null)
			throw new BadLoginException(BadLoginEnumException.BAD_CREDENTIALS, "Usuario inválido");
		if (!user.isEnable())
			throw new BadLoginException(BadLoginEnumException.DISABLED_USER, "Usuario inválido");
		if (!passwordEncryptor.matches(login.password, user.getPassword()))
			throw new BadLoginException(BadLoginEnumException.BAD_CREDENTIALS, "Usuario/contraseña inválida");
		log.debug("User {} authenticated", login.username);
		JWTokenBo result;
		if (featureFlagsService.isOn(AppFeature.HABILITAR_2FA) && fetchUserHasTwoFactorAuthenticationEnabled.run(user.getId())) {
			result = generatePartiallyAuthenticationToken.run(user.getId(), user.getUsername());
		} else {
			result = generateToken.generateTokens(user.getId(), user.getUsername());
			userInfoStorage.updateLoginDate(user.getUsername());
		}
		log.debug("Token generated");
		return result;
	}

}
