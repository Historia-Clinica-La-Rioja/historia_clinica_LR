package net.pladema.federar.services.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FederarValidateTokenPayload {
    
    @JsonProperty("accessToken")
    public final String accessToken;

	public FederarValidateTokenPayload(String accessToken) {
		this.accessToken = accessToken;
	}
    
}
