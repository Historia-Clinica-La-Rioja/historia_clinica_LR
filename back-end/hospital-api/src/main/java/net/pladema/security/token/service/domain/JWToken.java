package net.pladema.security.token.service.domain;

import java.io.Serializable;

public class JWToken implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1822742694596710178L;

	private String token;

	private String refreshToken;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}	
}
