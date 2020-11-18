package net.pladema.hl7.supporting.exchange.services.federar;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FederarLoginPayload {

    @JsonProperty("grantType")
    public final String grantType;

    @JsonProperty("scope")
    public final String scope;

    @JsonProperty("clientAssertionType")
    public final String clientAssertionType;
    
    @JsonProperty("clientAssertion")
    public final String clientAssertion;

	public FederarLoginPayload(String grantType, String scope, String clientAssertionType, String clientAssertion) {
		this.grantType = grantType;
		this.scope = scope;
		this.clientAssertionType = clientAssertionType;
		this.clientAssertion = clientAssertion;
	}
    
}
