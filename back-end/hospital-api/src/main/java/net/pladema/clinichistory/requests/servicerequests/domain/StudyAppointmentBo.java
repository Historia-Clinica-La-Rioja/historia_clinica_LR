package net.pladema.clinichistory.requests.servicerequests.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class StudyAppointmentBo {

	private Integer patientPersonId;
	private String patientFullName;
	private LocalDateTime actionTime;
	private Short statusId;
	private InformerObservationBo informerObservations;
	private InstitutionBasicInfoBo completionInstitution;
	private String technicianObservations;
	private Integer sizeImage;
	private Boolean isAvailableInPACS;
	private String imageId;

	public StudyAppointmentBo(Integer patientPersonId, LocalDateTime completedOn, String technicianObservations,
							  Integer completionInstitutionId, String completionInstitutionName) {
		this.patientPersonId = patientPersonId;
		this.actionTime = completedOn;
		this.statusId = EDiagnosticImageReportStatus.PENDING.getId();
		this.technicianObservations = technicianObservations;
		this.completionInstitution = new InstitutionBasicInfoBo(completionInstitutionId, completionInstitutionName);
		this.isAvailableInPACS = false;
	}
}
