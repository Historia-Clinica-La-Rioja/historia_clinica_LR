package ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddDiagnosticReportObservationCommandDto {
	private Integer procedureParameterId;
	private String value;
	private Short unitOfMeasureId;
	private String snomedSctid;
	private String snomedPt;
}