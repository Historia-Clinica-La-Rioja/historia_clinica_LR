package net.pladema.clinichistory.outpatient.createservicerequest.service.domain;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OutpatientServiceRequestBo {

	private PatientInfoBo patientInfo;

	private String categoryId;

	private Integer institutionId;

	private Integer doctorId;

	private Long noteId;

	private Integer medicalCoverageId;

	private List<DiagnosticReportBo> diagnosticReports;

	private LocalDateTime requestDate = LocalDateTime.now();

	public Integer getPatientId() {
		if (patientInfo != null)
			return patientInfo.getId();
		return null;
	}

}
