package net.pladema.clinichistory.requests.servicerequests.controller.dto.observations;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddDiagnosticReportObservationsCommandDto {
	private Boolean isPartialUpload;
	private Integer procedureTemplateId;
	private List<AddDiagnosticReportObservationCommandDto> values;
}
