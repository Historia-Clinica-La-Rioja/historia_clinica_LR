package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReferableConceptVo {

	private Short referableConceptId;

	private boolean isReferred;

}
