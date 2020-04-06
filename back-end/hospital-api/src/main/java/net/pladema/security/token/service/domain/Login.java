package net.pladema.security.token.service.domain;

import java.io.Serializable;

public class Login implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8314119814285504143L;

	private String username;

	private String password;

	public Login() {
		super();
	}
	
	public Login(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
