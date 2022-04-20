package ar.lamansys.sgh.shared.infrastructure.input.service;

import javax.validation.constraints.NotNull;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DietDto extends IndicationDto {

	@NotNull(message = "{value.mandatory}")
	private String description;

	public DietDto(Integer id, Integer patientId, Short typeId, Short statusId, Integer professionalId,String createdBy, DateDto indicationDate, DateTimeDto createdOn, String description) {
		super(id, patientId, EIndicationType.map(typeId), EIndicationStatus.map(statusId), professionalId, createdBy, indicationDate, createdOn);
		this.description = description;
	}

}
