package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PharmacoDto extends IndicationDto {

	@NotNull(message = "{value.mandatory}")
	private SharedSnomedDto snomed;

	@NotNull(message = "{value.mandatory}")
	private NewDosageDto dosage;

	@Nullable
	private OtherPharmacoDto solvent;

	@NotNull(message = "{value.mandatory}")
	private Integer healthConditionId;

	@NotNull(message = "{value.mandatory}")
	private Integer foodRelationId;

	@NotNull(message = "{value.mandatory}")
	private Boolean patientProvided;

	@NotNull(message = "{value.mandatory}")
	private Integer viaId;

	@Nullable
	private String note;

	public PharmacoDto(Integer id, Integer patientId, Short typeId, Short statusId,
					   Integer professionalId, String createdBy, DateDto indicationDate,
					   DateTimeDto createdOn, SharedSnomedDto snomed, NewDosageDto dosage,
					   OtherPharmacoDto solvent, Integer healthConditionId, Integer foodRelationId,
					   Boolean patientProvided, Integer viaId, String note) {
		super(id, patientId, EIndicationType.map(typeId), EIndicationStatus.map(statusId),
				professionalId, createdBy, indicationDate, createdOn);
		this.snomed = snomed;
		this.dosage = dosage;
		this.solvent = solvent;
		this.healthConditionId = healthConditionId;
		this.foodRelationId = foodRelationId;
		this.patientProvided = patientProvided;
		this.viaId = viaId;
		this.note = note;
	}

}
