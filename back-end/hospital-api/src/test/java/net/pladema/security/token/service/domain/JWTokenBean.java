package net.pladema.security.token.service.domain;

public class JWTokenBean {
	private JWTokenBean() {

	}

	public static JWToken newJWToken(String token, String refreshtoken) {
		JWToken jwToken = new JWToken();
		jwToken.setToken(token);
		jwToken.setRefreshToken(refreshtoken);
		return jwToken;
	}
}
