package ar.lamansys.sgx.shared.scheduling.infrastructure.output.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TAWSResponseError {
	
	@JsonProperty("location")
    private String location;
	
	@JsonProperty("msg")
	private String msg;
	
	@JsonProperty("param")
	private String param;
	
	@JsonProperty("value")
	private String value;

	public TAWSResponseError() {
		
	}
	
	public TAWSResponseError(String location, String msg, String param, String value) {
		this.location = location;
		this.msg = msg;
		this.param = param;
		this.value = value;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "TAWSResponseError [location=" + location + ", msg=" + msg + ", param=" + param + ", value=" + value
				+ "]";
	}
	
}
