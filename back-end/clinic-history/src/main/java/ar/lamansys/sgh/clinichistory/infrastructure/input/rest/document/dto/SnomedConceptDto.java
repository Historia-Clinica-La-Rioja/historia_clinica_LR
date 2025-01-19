package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SnomedConceptDto {

	private String pt;
	private Boolean isMainHealthCondition;
}
