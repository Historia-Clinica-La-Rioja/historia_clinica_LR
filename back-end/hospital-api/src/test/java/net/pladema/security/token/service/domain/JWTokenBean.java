package net.pladema.security.token.service.domain;

public class JWTokenBean {

	private JWTokenBean() { }

	public static JWToken newJWToken(String token, String refreshtoken) {
		return new JWToken(token, refreshtoken);
	}
}
