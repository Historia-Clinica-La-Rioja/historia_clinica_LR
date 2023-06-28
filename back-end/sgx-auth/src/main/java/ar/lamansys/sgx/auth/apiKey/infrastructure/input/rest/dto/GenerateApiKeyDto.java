package ar.lamansys.sgx.auth.apiKey.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class GenerateApiKeyDto {
	public final String name;
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public GenerateApiKeyDto(
			@JsonProperty("name") String name
	) {
		this.name = name;
	}
}
