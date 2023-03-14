package ar.lamansys.sgx.shared.restclient.configuration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtils {

	private JWTUtils(){
		super();
	}

	public static String generateJWT(Map<String, String> claims, String signKey, Long secondsAlive) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expirationTime = now.plusSeconds(secondsAlive);
		Map<String,Object> claimsObj = new HashMap<>(claims);
		return Jwts.builder().setClaims(claimsObj)
				.setIssuedAt(Date.from(now.toInstant(ZoneOffset.UTC)))
				.setExpiration(Date.from(expirationTime.toInstant(ZoneOffset.UTC)))
				.signWith(SignatureAlgorithm.HS256,signKey.getBytes())
				.compact();
	}

}
