package net.pladema.cipres.infrastructure.output.rest.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresEntityResponse;

import java.util.Map;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CipresCityResponse extends CipresEntityResponse {

	private Map<String, Object> partido;

	private String descripcionLocalidad;
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public CipresCityResponse(@JsonProperty("partido") Map<String, Object> partido) {
		this.partido = partido;
	}

}
