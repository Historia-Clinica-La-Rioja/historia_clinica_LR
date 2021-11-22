package ar.lamansys.sgx.auth.jwt.domain;

import lombok.Getter;

@Getter
public class LoginBo {

	public final String username;
	public final String password;

	public LoginBo(String username, String password) {
		this.username = username;
		this.password = password;
	}

}
