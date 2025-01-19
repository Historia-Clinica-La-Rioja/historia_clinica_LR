package net.pladema.cipres.infrastructure.output.rest.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CipresPatientResponse {

	private Map<String, Object> paciente;
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public CipresPatientResponse(@JsonProperty("paciente") Map<String, Object> paciente) {
		this.paciente = paciente;
	}
}
