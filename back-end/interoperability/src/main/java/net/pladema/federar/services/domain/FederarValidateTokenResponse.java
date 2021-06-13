package net.pladema.federar.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FederarValidateTokenResponse {

	private String organizationUrl;
	private String name;
	private String ident;
	private String role;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public FederarValidateTokenResponse(@JsonProperty("organizationUrl") String organizationUrl,
			@JsonProperty("name") String name, @JsonProperty("ident") String ident, @JsonProperty("role") String role) {
		this.organizationUrl = organizationUrl;
		this.name = name;
		this.ident = ident;
		this.role = role;
	}

}
