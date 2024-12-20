package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DiagnosticReportVo {

	private String id;
	private String serviceRequestId;
	private String status;
	private String statusId;
	private String categoryCode;
	private String code;
	private String patientId;
//	private String doctorId;
	private String diagnosticReportId;
	private UUID diagnosticReportUuid;
	private UUID serviceRequestUuid;
	private List<ObservationVo> observations = new ArrayList<>();
	private String conclusion;
	private String conclusionCode;

	private PatientVo patientVo;
	private PractitionerVo practitionerVo;
	private LocationVo locationVo;
	private OrganizationVo organizationVo;
	private CoverageVo coverageVo;
	private ServiceRequestVo serviceRequestVo;

	/**
	 * The performer of the diagnostic report can be a practitioner,
	 * an organization or both.
	 */
	private List<PractitionerVo> performerPractitioners;
	private List<OrganizationVo> performerOrganizations;

	public void addObservation(ObservationVo observationVo) {
		observations.add(observationVo);
	}

}
