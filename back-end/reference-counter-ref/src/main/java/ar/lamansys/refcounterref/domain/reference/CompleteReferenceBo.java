package ar.lamansys.refcounterref.domain.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CompleteReferenceBo extends ReferenceBo {

	private Integer institutionId;

	private Integer patientId;

	private Integer patientMedicalCoverageId;

	private Integer doctorId;

	private Integer oldReferenceId;

}
