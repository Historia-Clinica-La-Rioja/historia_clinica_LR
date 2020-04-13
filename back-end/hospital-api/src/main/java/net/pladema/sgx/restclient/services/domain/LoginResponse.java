package net.pladema.sgx.restclient.services.domain;


public class LoginResponse {

	protected String token;
	
	public LoginResponse() {
	}

	public LoginResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
