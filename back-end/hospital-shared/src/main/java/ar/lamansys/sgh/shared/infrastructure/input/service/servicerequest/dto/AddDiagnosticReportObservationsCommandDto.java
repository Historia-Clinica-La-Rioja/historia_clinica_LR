package ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceClosureDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddDiagnosticReportObservationsCommandDto {
	private Boolean isPartialUpload;
	private Integer procedureTemplateId;
	private List<AddDiagnosticReportObservationCommandDto> values;
	private ReferenceClosureDto referenceClosure;
}
