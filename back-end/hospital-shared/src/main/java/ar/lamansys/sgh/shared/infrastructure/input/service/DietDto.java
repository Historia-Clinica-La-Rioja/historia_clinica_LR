package ar.lamansys.sgh.shared.infrastructure.input.service;

import javax.validation.constraints.NotNull;

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

	public DietDto(Integer id, Integer patientId, Short typeId, Short statusId, String createdBy, DateTimeDto indicationDate, String description) {
		super(id, patientId, EIndicationType.map(typeId), EIndicationStatus.map(statusId), createdBy, indicationDate);
		this.description = description;
	}

}
