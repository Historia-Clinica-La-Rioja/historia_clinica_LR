package ar.lamansys.sgx.shared.restclient.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WSResponse {

	public final String msg;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public WSResponse(String msg) {
		this.msg = msg;
	}
}
