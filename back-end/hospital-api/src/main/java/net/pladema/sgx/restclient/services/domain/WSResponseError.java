package net.pladema.sgx.restclient.services.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WSResponseError {
	
	@JsonProperty("msg")
	private String msg;

	public WSResponseError() {
		
	}
	
	public WSResponseError(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return "WSResponseError [ msg=" + msg + "]";
	}
	
}
