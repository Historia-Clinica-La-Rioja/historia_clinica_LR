package ar.lamansys.sgx.shared.restclient.configuration;

import java.time.Duration;

public class TokenHolder {
	private Duration validTime;
	private long tokenDueTimestamp;
	private String token = null;


	public TokenHolder(Duration duration) {
		this.validTime =  duration;
	}

	public boolean isValid() {
		if (this.validTime.isNegative()) {
			return true;
		}
		return token != null && (System.currentTimeMillis() < this.tokenDueTimestamp);
	}

	public String get() {
		return token;
	}

	public void set(String token) {
		this.token = token;
		this.tokenDueTimestamp = System.currentTimeMillis() + validTime.toMillis();
	}
}
