package ar.lamansys.sgx.auth.twoFactorAuthentication.application.validateTwoFactorAuthentication;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.CypherStorage;
import ar.lamansys.sgx.auth.twoFactorAuthentication.application.UserAuthenticationStorage;
import ar.lamansys.sgx.shared.security.UserInfo;
import de.taimos.totp.TOTP;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidateTwoFactorAuthenticationCode {

	private final UserAuthenticationStorage userAuthenticationStorage;
	private final CypherStorage cypher;

	private String getTotpCode(String secretKey) {
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(secretKey);
		String hexKey = Hex.encodeHexString(bytes);
		return TOTP.getOTP(hexKey);
	}

	public boolean run(String code) {
		log.debug("Input parameter -> code {}", code);
		Integer userId = UserInfo.getCurrentAuditor();
		Optional<String> opSecret = userAuthenticationStorage.getTwoFactorAuthenticationSecret(userId);
		if (opSecret.isPresent()) {
			String decryptedSecret = cypher.decrypt(opSecret.get());
			String totpCode = getTotpCode(decryptedSecret);
			return totpCode.equals(code);
		}
		return true;
	}
}
