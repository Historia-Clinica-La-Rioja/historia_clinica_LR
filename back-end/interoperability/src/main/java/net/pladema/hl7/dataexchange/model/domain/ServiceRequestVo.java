package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ServiceRequestVo {

	private Integer srId;
	private String serviceRequestStatus;
	private String intentId;
	private String categoryId;
	private Integer diagnosticReportId;
	private String patientId;
	private String patientIdentificationNumber;
	private Integer institutionId;
	private Date requestDate;
	private Integer doctorId;
	private String problemId;
	private String problemPt;
	private Integer patientMedicalCoverageId;
	private Integer medicalCoverageId;
	private String coverageAffiliateNumber;
	private String description;
	private String link;
	private String diagnosticReportStatus;
	private String snomedId;
	private String snomedPt;
	private UUID serviceRequestUuid;
	private UUID diagnosticReportUuid;
	private Integer diagnosticReportParentId;

	public ServiceRequestVo(Integer srId, String serviceRequestStatus, String intentId,
		String categoryId, Integer diagnosticReportId, Integer patientId, Integer institutionId,
		Date requestDate, Integer doctorId, String problemId, String problemPt, Integer medicalCoverageId,
		String description, String link, String diagnosticReportStatus, String snomedId,
		String snomedPt, UUID serviceRequestUuid, UUID diagnosticReportUuid,
		Integer diagnosticReportParentId
	) {
		this.srId = srId;
		this.serviceRequestStatus = serviceRequestStatus;
		this.intentId = intentId;
		this.categoryId = categoryId;
		this.diagnosticReportId = diagnosticReportId;
		this.patientId = patientId.toString();
		this.institutionId = institutionId;
		this.requestDate = requestDate;
		this.doctorId = doctorId;
		this.problemId = problemId;
		this.problemPt = problemPt;
		this.medicalCoverageId = medicalCoverageId;
		this.description = description;
		this.link = link;
		this.diagnosticReportStatus = diagnosticReportStatus;
		this.snomedId = snomedId;
		this.snomedPt = snomedPt;
		this.serviceRequestUuid = serviceRequestUuid;
		this.diagnosticReportUuid = diagnosticReportUuid;
		this.diagnosticReportParentId = diagnosticReportParentId;
	}

	public ServiceRequestVo(Integer srId, Integer diagnosticReportId, Integer patientId,
		String patientIdentificationNumber, Integer institutionId, Date requestDate, Integer doctorId,
		Integer patientMedicalCoverageId, Integer medicalCoverageId, String coverageAffiliateNumber,
		String serviceRequestStatus, String diagnosticReportStatus) {
		this.srId = srId;
		this.diagnosticReportId = diagnosticReportId;
		this.patientId = patientId.toString();
		this.patientIdentificationNumber = patientIdentificationNumber;
		this.institutionId = institutionId;
		this.requestDate = requestDate;
		this.doctorId = doctorId;
		this.patientMedicalCoverageId = patientMedicalCoverageId;
		this.medicalCoverageId = medicalCoverageId;
		this.coverageAffiliateNumber = coverageAffiliateNumber;
		this.serviceRequestStatus = serviceRequestStatus;
		this.diagnosticReportStatus = diagnosticReportStatus;
	}

	public boolean hasUuid() {
		return this.getServiceRequestUuid() != null;
	}
}
