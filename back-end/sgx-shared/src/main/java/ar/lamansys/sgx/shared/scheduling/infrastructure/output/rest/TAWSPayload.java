package ar.lamansys.sgx.shared.scheduling.infrastructure.output.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TAWSPayload {

	@JsonProperty("_id")
	protected String idExterno;

	public String getIdExterno() {
		return idExterno;
	}

	public void setIdExterno(String idExterno) {
		this.idExterno = idExterno;
	}
	
	public boolean alreadyExists() {
		return idExterno != null;
	}

}
