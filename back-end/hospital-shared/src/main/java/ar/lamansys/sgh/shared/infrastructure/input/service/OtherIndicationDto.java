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
public class OtherIndicationDto extends IndicationDto {

	@NotNull(message = "{value.mandatory}")
	private Short otherIndicationTypeId;

	@NotNull(message = "{value.mandatory}")
	private String description;

	@Nullable
	private NewDosageDto dosage;

	@Nullable
	private String otherType;

	public OtherIndicationDto(Integer id, Integer patientId, Short typeId, Short statusId, Integer professionalId, String createdBy, DateDto indicationDate, DateTimeDto createdOn, Short otherIndicationTypeId, String description, NewDosageDto dosage, String otherType) {
		super(id, patientId, EIndicationType.map(typeId), EIndicationStatus.map(statusId), professionalId, createdBy, indicationDate, createdOn);
		this.otherIndicationTypeId = otherIndicationTypeId;
		this.description = description;
		this.dosage = dosage;
		this.otherType = otherType;
	}

}
