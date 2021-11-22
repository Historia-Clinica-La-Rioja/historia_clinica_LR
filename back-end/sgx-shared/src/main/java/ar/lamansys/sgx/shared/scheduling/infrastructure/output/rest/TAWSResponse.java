package ar.lamansys.sgx.shared.scheduling.infrastructure.output.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TAWSResponse {

	public final String externalId;
	public final int __v;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public TAWSResponse(String externalId, int __v) {
		this.externalId = externalId;
		this.__v = __v;
	}
}
