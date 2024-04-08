package ar.lamansys.sgh.clinichistory.domain.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SnomedConceptBo {

	private String pt;
	private Boolean isMainHealthCondition;
}
