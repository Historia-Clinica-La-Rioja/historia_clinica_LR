package net.pladema.security.utils;

import java.util.Map;

import org.springframework.util.Assert;

import io.jsonwebtoken.Jwts;
import net.pladema.security.exceptions.JWTParseException;

public class JWTUtils {
	private JWTUtils() {}

	public static Map<String, Object> parse(String token, String secret) throws JWTParseException {
		try {
			Map<String, Object> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
			Assert.notNull(claims, "Token inválido");
			return claims;
		} catch (Throwable e) {
			throw new JWTParseException(e);
		}
	}
}
