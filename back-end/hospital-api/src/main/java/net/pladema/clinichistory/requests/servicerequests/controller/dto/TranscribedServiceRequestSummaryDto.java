package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TranscribedServiceRequestSummaryDto {

	@NotNull(message = "${value.mandatory}")
	private Integer serviceRequestId;

	private List<DiagnosticReportSummaryDto> diagnosticReports = new ArrayList<>();

}
