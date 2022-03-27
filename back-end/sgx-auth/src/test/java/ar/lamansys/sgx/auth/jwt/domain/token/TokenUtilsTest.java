package ar.lamansys.sgx.auth.jwt.domain.token;

import static ar.lamansys.sgx.auth.SgxAsserts.assertPresent;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Map;

import ar.lamansys.sgx.auth.jwt.infrastructure.output.token.TokenUtils;
import ar.lamansys.sgx.shared.token.JWTUtils;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
class TokenUtilsTest {

	private final Map<String, Object> defaultClaims = Map.of(
			JWTUtils.TOKEN_CLAIM_TYPE, ETokenType.NORMAL
	);
	private final String defaultSubject = "admin@example.net";
	private final String defaultSecret = "shhhh";
	private final Duration defaultFutureExpiration = Duration.ofHours(1);
	private final Duration defaultDueExpiration = Duration.ofHours(-1);

	@Test
	void testWrongSecretShouldNotWork() {
		assertFalse(
				JWTUtils.parseClaims(
						JWTUtils.generate(defaultClaims, defaultSubject, defaultSecret, defaultDueExpiration),
						"hhhh"
				).isPresent()
		);
	}

	@Test
	void testExpiredTokenShouldNotWork() {
		assertFalse(
			JWTUtils.parseClaims(
					JWTUtils.generate(defaultClaims, defaultSubject, defaultSecret, defaultDueExpiration),
					"shhhh"
			).isPresent()
		);
	}

	@Test
	void testSingleSuccessTest() {
		Map<String, Object> claimsParsed = generateThenParse(defaultClaims, defaultSubject, defaultSecret, defaultFutureExpiration);
		assertFalse(
				claimsParsed.isEmpty()
		);
		assertEquals(
				claimsParsed.get(JWTUtils.TOKEN_CLAIM_TYPE),
				ETokenType.NORMAL.toString()
		);
	}

	private static Map<String, Object> generateThenParse(Map<String, Object> claims, String subject, String secret, Duration expiration) {
		return assertPresent(
				JWTUtils.parseClaims(
						JWTUtils.generate(claims, subject, secret, expiration),
						secret
				)
		);
	}

	@Test
	void isTokenType_okWhenCorrect() {
		assertTrue(
				TokenUtils.isTokenType(
						ETokenType.VERIFICATION,
						claimsWith(ETokenType.VERIFICATION.toString())
				)
		);
	}

	@Test
	void isTokenType_failWhenDifferentTokenType() {
		assertFalse(
				TokenUtils.isTokenType(
						ETokenType.VERIFICATION,
						claimsWith("sarasa")
				)
		);
	}

	private static Map<String, Object> claimsWith(String tokenType) {
		return Map.of(JWTUtils.TOKEN_CLAIM_TYPE, tokenType);
	}

}