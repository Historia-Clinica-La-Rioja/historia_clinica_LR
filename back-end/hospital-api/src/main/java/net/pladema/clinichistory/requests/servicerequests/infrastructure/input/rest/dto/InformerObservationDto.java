package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ConclusionDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InformerObservationDto {

	private Short id;
	private String evolutionNote;
	private List<ConclusionDto> conclusions;
	private String createdBy;
	private DateTimeDto actionTime;
	private boolean confirmed;

}
