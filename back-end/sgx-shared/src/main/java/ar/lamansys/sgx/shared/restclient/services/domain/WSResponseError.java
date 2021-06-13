package ar.lamansys.sgx.shared.restclient.services.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WSResponseError implements Serializable {
	
	@JsonProperty("msg")
	private String msg;

	public WSResponseError() {
		super();
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
