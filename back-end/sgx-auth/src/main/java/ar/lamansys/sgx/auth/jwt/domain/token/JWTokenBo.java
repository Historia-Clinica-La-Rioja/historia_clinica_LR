package ar.lamansys.sgx.auth.jwt.domain.token;

public class JWTokenBo {

	public final String token;
	public final String refreshToken;

	public JWTokenBo(String token, String refreshToken) {
		this.token = token;
		this.refreshToken = refreshToken;
	}

}
