package net.pladema.hl7.supporting.exchange.services.federar;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

	private JWTUtils(){
		super();
	}

	public static String generateJWT(Map<String, String> claims, String signKey, Integer secondsAlive) {
		Calendar cal = Calendar.getInstance();
		Date issuedAt = cal.getTime();
		cal.add(Calendar.SECOND, secondsAlive);
		Date expirationTime = cal.getTime();
		Map<String,Object> claimsObj = new HashMap<>(claims);
		return Jwts.builder().setClaims(claimsObj)
				.setIssuedAt(issuedAt).setExpiration(expirationTime)
				.signWith(SignatureAlgorithm.HS256,signKey.getBytes())
				.compact();
	}

}
