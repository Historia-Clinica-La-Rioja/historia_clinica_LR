package net.pladema.cipres.infrastructure.output.rest.domain.consultation;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CipresEntityResponse {

	private String id;

}
