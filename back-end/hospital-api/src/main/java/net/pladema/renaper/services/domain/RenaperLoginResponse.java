package net.pladema.renaper.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.restclient.services.domain.LoginResponse;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RenaperLoginResponse implements LoginResponse {

	private String token;
	
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public RenaperLoginResponse(@JsonProperty("token") String token) {
		this.token = token;
	}

	
}
