package ar.lamansys.sgx.auth.twoFactorAuthentication.application.validateTwoFactorAuthentication;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.CypherStorage;
import ar.lamansys.sgx.auth.twoFactorAuthentication.application.UserAuthenticationStorage;
import ar.lamansys.sgx.shared.security.UserInfo;
import de.taimos.totp.TOTP;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidateTwoFactorAuthenticationCode {

	private final UserAuthenticationStorage userAuthenticationStorage;
	private final CypherStorage cypher;

	@Value("${auth.2fa.defaultkey:}")
	private String DEFAULT_2FA_CODE;

	private String getTotpCode(String secretKey) {
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(secretKey);
		String hexKey = Hex.encodeHexString(bytes);
		return TOTP.getOTP(hexKey);
	}

	public boolean run(String code) {
		log.debug("Input parameter -> code {}", code);
		if (code.equals(DEFAULT_2FA_CODE))
			return true;
		Integer userId = UserInfo.getCurrentAuditor();
		Optional<String> opSecret = userAuthenticationStorage.getTwoFactorAuthenticationSecret(userId);
		return opSecret.map(secret -> handle2FACode(code, secret)).orElse(true);
	}

	private boolean handle2FACode(String code, String opSecret) {
		String decryptedSecret = cypher.decrypt(opSecret);
		String totpCode = getTotpCode(decryptedSecret);
		return totpCode.equals(code);
	}

}
