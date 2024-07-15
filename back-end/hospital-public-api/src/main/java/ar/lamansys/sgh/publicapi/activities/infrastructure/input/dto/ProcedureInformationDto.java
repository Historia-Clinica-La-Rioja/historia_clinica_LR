package ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto;

import java.io.Serializable;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ProcedureInformationDto implements Serializable {

	private SnomedDto snomed;
	private DateTimeDto date;
}
