package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ToString
public class CompleteReferenceStudyDto {

	@NotNull
	private SharedSnomedDto practice;

	@NotNull
	private String categoryId;

	@NotNull
	private Integer patientId;

	@Nullable
	private Integer patientMedicalCoverageId;

	@NotNull
	private Integer healthConditionId;

	@NotNull
	private Integer doctorId;

	@NotNull
	private Integer institutionId;

	@NotNull
	private Integer encounterId;

	@NotNull
	private Integer sourceTypeId;

}
