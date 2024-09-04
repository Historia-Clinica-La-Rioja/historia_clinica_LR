package net.pladema.clinichistory.requests.servicerequests.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
public class StudyAppointmentBo {

	private final static String DNI_CHAR_SEQUENCE_TO_REPLACE = "{dni}";

	private Integer patientId;
	private Integer personId;
	private String patientFullName;
	private LocalDateTime actionTime;
	private Short statusId;
	private InformerObservationBo informerObservations;
	private InstitutionBasicInfoBo completionInstitution;
	private String technicianObservations;
	private Integer sizeImage;
	private Boolean isAvailableInPACS;
	private String imageId;
	private String localViewerUrl;

	public StudyAppointmentBo(Integer patientId, Integer personId, LocalDateTime completedOn, String technicianObservations,
							  Integer completionInstitutionId, String completionInstitutionName, String localViewerUrl, String identificationNumber) {
		this.patientId = patientId;
		this.personId = personId;
		this.actionTime = completedOn;
		this.statusId = EDiagnosticImageReportStatus.PENDING.getId();
		this.technicianObservations = technicianObservations;
		this.completionInstitution = new InstitutionBasicInfoBo(completionInstitutionId, completionInstitutionName);
		this.isAvailableInPACS = false;
		this.localViewerUrl = completeLocalViewerUrl(localViewerUrl,identificationNumber);
	}

	private String completeLocalViewerUrl(String localViewerUrl, String identificationNumber) {
		if (Objects.isNull(localViewerUrl)) {
			return null;
		}
		String dni = Objects.isNull(identificationNumber) ? "" : identificationNumber;
		return localViewerUrl.toLowerCase().replace(DNI_CHAR_SEQUENCE_TO_REPLACE,dni);
	}
}
