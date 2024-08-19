package net.pladema.clinichistory.requests.servicerequests.controller.dto.observations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetDiagnosticReportObservationGroupDto {
	Integer id;
	Integer diagnosticReportId;
	Integer procedureTemplateId;
	Boolean isPartialUpload;
	List<GetDiagnosticReportObservationDto> observations;
}
