package ar.lamansys.sgx.auth.twoFactorAuthentication.application.generateTwoFactorAuthentication;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.CypherStorage;
import ar.lamansys.sgx.auth.twoFactorAuthentication.application.UserAuthenticationStorage;
import ar.lamansys.sgx.auth.twoFactorAuthentication.application.exceptions.TwoFactorAuthenticationException;
import ar.lamansys.sgx.auth.twoFactorAuthentication.application.exceptions.TwoFactorAuthenticationExceptionEnum;
import ar.lamansys.sgx.auth.twoFactorAuthentication.domain.SetTwoFactorAuthenticationBo;
import ar.lamansys.sgx.shared.security.UserInfo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GenerateTwoFactorAuthentication {

	private final UserAuthenticationStorage userAuthenticationStorage;
	private final CypherStorage cypher;
	private final String issuer;

	public GenerateTwoFactorAuthentication(UserAuthenticationStorage userAuthenticationStorage,
										   CypherStorage cypher,
										   @Value("${auth.2fa.issuer:HSI}") String issuer) {
		this.userAuthenticationStorage = userAuthenticationStorage;
		this.cypher = cypher;
		this.issuer = issuer;
	}

	public SetTwoFactorAuthenticationBo run() {
		log.debug("Set two factor authentication");
		Integer userId = UserInfo.getCurrentAuditor();
		if (userAuthenticationStorage.userHasTwoFactorAuthenticationEnabled(userId))
			throw new TwoFactorAuthenticationException(TwoFactorAuthenticationExceptionEnum.TWO_FACTOR_AUTHENTICATION_ALREADY_ENABLED,
					"El usuario ya tiene la autenticación por doble factor activada");
		String username = userAuthenticationStorage.getUsername(userId);
		String secretKey = userAuthenticationStorage.generateSecretKey();
		String encryptedSecret = cypher.encrypt(secretKey);
		userAuthenticationStorage.setTwoFactorAuthenticationSecret(userId, encryptedSecret);
		SetTwoFactorAuthenticationBo result = new SetTwoFactorAuthenticationBo(username, issuer, secretKey);
		log.debug("Output -> {}", result);
		return result;
	}


}
