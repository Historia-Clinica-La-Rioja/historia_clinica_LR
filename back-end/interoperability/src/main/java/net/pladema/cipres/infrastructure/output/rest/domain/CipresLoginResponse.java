package net.pladema.cipres.infrastructure.output.rest.domain;

import ar.lamansys.sgx.shared.restclient.services.domain.LoginResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CipresLoginResponse implements LoginResponse {

	private String token;

	private CipresLoginResponse(@JsonProperty("token") String token) {
		this.token = token;
	}

	@Override
	public String getToken() {
		return this.token;
	}
}
