package ar.lamansys.sgx.shared.token;

import java.nio.charset.StandardCharsets;
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
			Map<String, Object> claims = Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
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
				.signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
				.compact();
	}

	private static Date generateExpirationDate(Duration expiration) {
		return new Date(System.currentTimeMillis() + expiration.toMillis());
	}
}
