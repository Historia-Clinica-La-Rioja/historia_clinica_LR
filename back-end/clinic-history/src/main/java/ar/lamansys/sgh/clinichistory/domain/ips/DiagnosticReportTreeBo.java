package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Value;

@Value
public class DiagnosticReportTreeBo {
	private Integer diagnosticReportParentId;
	private Integer diagnosticReportChildId;

}
