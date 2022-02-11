package ar.lamansys.sgx.auth.jwt.infrastructure.output.token;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.auth.jwt.domain.token.TokenData;
import ar.lamansys.sgx.shared.token.JWTUtils;

public class TokenUtils {

	public static final String TOKEN_CLAIM_TYPE = "tokentype";

	private TokenUtils() {}

	private static Date generateExpirationDate(Duration expiration) {
		return new Date(System.currentTimeMillis() + expiration.toMillis());
	}

	public static Optional<TokenData> parseToken(String token, String secret, ETokenType expectedType) {
		return JWTUtils.parseClaims(token, secret)
				.filter(claims -> isTokenType(expectedType, claims))
				.map(
						claims -> new TokenData(
								expectedType,
								claims.get("sub").toString(),
								(Integer)claims.get("userId")
						)
				);
	}

	public static boolean isTokenType(ETokenType expected, Map<String, Object> claims) {
		Object found = claims.get(TOKEN_CLAIM_TYPE);
		return (expected.toString().equals(found));
	}
}
