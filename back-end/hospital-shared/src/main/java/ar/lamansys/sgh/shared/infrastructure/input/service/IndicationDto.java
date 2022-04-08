package ar.lamansys.sgh.shared.infrastructure.input.service;

import javax.validation.constraints.NotNull;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IndicationDto {

	private Integer id;

	@NotNull(message = "{value.mandatory}")
	private Integer patientId;

	@NotNull(message = "{value.mandatory}")
	private EIndicationType type;

	@NotNull(message = "{value.mandatory}")
	private EIndicationStatus status;

	private Integer professionalId;

	private String createdBy;

	private DateDto indicationDate;

	private DateTimeDto createdOn;

}
