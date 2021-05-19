package ar.lamansys.sgx.shared.restclient.configuration;

public class TokenHolder {
	private long validTime;
	private long tokenDueTimestamp;
	private String token = null;


	public TokenHolder(long seconds) {
		this.validTime =  1000 * seconds;
	}

	public boolean isValid() {
		if (this.validTime < 0) {
			return true;
		}
		return token != null && (System.currentTimeMillis() < this.tokenDueTimestamp);
	}

	public String get() {
		return token;
	}

	public void set(String token) {
		this.token = token;
		this.tokenDueTimestamp = System.currentTimeMillis() + validTime;
	}
}
