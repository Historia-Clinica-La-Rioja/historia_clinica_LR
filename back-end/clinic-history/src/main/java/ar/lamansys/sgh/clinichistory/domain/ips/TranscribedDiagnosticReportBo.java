package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranscribedDiagnosticReportBo {
	private Integer serviceRequestId;
	private Integer studyId;
	private String studyName;
}
