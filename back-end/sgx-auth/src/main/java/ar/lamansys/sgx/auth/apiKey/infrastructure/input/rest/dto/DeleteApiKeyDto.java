package ar.lamansys.sgx.auth.apiKey.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteApiKeyDto {
	public final String name;
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public DeleteApiKeyDto(
			@JsonProperty("name") String name
	) {
		this.name = name;
	}
}
