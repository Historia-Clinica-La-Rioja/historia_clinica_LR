package net.pladema.federar.services.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FederarIdentifierPayload {

	private String use;
	private String system;
	private String value;
	
	public FederarIdentifierPayload(String system, String value) {
		this.system = system;
		this.value = value;
	}
	
}
