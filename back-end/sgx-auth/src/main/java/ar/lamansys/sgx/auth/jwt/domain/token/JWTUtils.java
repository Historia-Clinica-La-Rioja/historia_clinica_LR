package ar.lamansys.sgx.auth.jwt.domain.token;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.util.Assert;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtils {

	public static final String TOKEN_CLAIM_TYPE = "tokentype";

	private JWTUtils() {}

	public static Optional<Map<String, Object>> parseClaims(String token, String secret) {
		try {
			Map<String, Object> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
			Assert.notNull(claims, "Token inválido");
			return Optional.of(claims);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public static String generate(Map<String, Object> claims, String subject, String secret, Duration expiration) {
		Date expirationDate = generateExpirationDate(expiration);
		return Jwts.builder()
				.addClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

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
