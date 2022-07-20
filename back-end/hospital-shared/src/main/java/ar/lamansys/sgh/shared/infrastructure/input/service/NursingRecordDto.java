package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NursingRecordDto {

	private Integer id;

	@NotNull(message = "{value.mandatory}")
	private IndicationDto indication;

	@NotNull(message = "{value.mandatory}")
	private ENursingRecordStatus status;

	private DateTimeDto scheduledAdministrationTime;

	private String event;

	private String observation;

	private DateTimeDto administrationTime;

	private String updatedBy;

	private String updateReason;

}





