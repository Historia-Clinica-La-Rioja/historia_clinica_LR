package net.pladema.clinichistory.requests.servicerequests.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceRequestProcedureInfoBo {

	private Integer serviceRequestId;

	private SnomedBo procedure;

	private Integer diagnosticReportId;

	private String statusId;

	public ServiceRequestProcedureInfoBo(Integer serviceRequestId, Integer snomedId,
										 String sctid, String pt, Integer diagnosticReportId, String statusId) {
		this.serviceRequestId = serviceRequestId;
		this.procedure = new SnomedBo(snomedId, sctid, pt);
		this.diagnosticReportId = diagnosticReportId;
		this.statusId = statusId;
	}
}
