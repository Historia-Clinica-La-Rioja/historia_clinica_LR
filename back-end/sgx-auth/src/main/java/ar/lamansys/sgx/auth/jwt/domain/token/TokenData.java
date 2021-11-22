package ar.lamansys.sgx.auth.jwt.domain.token;

public class TokenData {

	public final ETokenType type;

	public final String username;

	public final Integer userId;

	public TokenData(ETokenType type, String username, Integer userId) {
		this.type = type;
		this.username = username;
		this.userId = userId;
	}

	public boolean isType(ETokenType type) {
		return this.type == type;
	}
}
