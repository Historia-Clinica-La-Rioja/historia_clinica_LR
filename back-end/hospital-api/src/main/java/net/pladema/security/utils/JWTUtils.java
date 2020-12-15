package net.pladema.security.utils;

import java.util.Map;

import org.springframework.util.Assert;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import net.pladema.security.exceptions.JWTParseException;

public class JWTUtils {
	private JWTUtils() {}

	public static Map<String, Object> parse(String token, String secret) {
		try {
			Map<String, Object> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
			Assert.notNull(claims, "Token inválido");
			return claims;
		} catch (Exception e) {
			throw new JWTParseException(e);
		}
	}
}
