package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompleteReferenceDto extends ReferenceDto {

	private Integer institutionId;

	private Integer patientId;

	private Integer patientMedicalCoverageId;

	private Integer doctorId;

	private Integer encounterId;

	private Integer sourceTypeId;

}
