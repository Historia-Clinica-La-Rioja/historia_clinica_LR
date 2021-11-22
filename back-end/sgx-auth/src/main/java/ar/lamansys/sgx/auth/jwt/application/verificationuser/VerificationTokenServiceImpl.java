package ar.lamansys.sgx.auth.jwt.application.verificationuser;


import java.time.Duration;
import java.util.Map;

import ar.lamansys.sgx.auth.jwt.domain.token.JWTUtils;
import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
	private final Logger logger;
	private final String secret;
	private final Duration validationTokenExpiration;

	public VerificationTokenServiceImpl(
			@Value("${token.secret}") String secret,
			@Value("${validationToken.expiration}") Duration validationTokenExpiration
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.secret = secret;
		this.validationTokenExpiration = validationTokenExpiration;
	}

	@Override
	public String generateVerificationToken(Integer userId, String username) {
		logger.debug("Generate verification token to userid/username {}/{}", userId, username);
		Map<String, Object> claims = Map.of(
				"userId", userId,
				JWTUtils.TOKEN_CLAIM_TYPE, ETokenType.VERIFICATION
		);
		return JWTUtils.generate(claims, username, secret, validationTokenExpiration);
	}

	@Override
	public boolean validVerificationToken(String verificationToken) {
		return JWTUtils.parseClaims(verificationToken, secret)
			.map(claims -> JWTUtils.isTokenType(ETokenType.VERIFICATION, claims))
			.orElse(false);

	}
}
