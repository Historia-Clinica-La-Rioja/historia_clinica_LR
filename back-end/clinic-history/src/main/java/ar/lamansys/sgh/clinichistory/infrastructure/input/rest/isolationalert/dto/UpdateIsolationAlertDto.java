package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.dto;

import javax.annotation.Nullable;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateIsolationAlertDto {
	Short criticalityId;
	DateDto endDate;
	@Nullable
	String observations;
}
