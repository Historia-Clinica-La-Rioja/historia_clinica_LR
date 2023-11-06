package net.pladema.clinichistory.documents.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CHDocumentBo {

	protected static final String SPECIAL_CHARACTER = ":";

	private Long id;
	private Integer sourceId;
	private Integer patientId;
	private LocalDateTime createdOn;
	private Integer createdBy;
	private Short documentTypeId;
	private Short sourceTypeId;
	private Integer requestSourceId;
	private String clinicalSpecialty;
	private Integer institutionId;
	private String institution;
	private Short requestSourceTypeId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String patientAgePeriod;
	private ECHEncounterType encounterType;
	private ECHDocumentType documentType;

	public CHDocumentBo(VClinicHistory vClinicHistory, ECHEncounterType encounterType, ECHDocumentType documentType){
		this.id = vClinicHistory.getId();
		this.sourceId = vClinicHistory.getSourceId();
		this.patientId = vClinicHistory.getPatientId();
		this.createdOn = vClinicHistory.getCreatedOn();
		this.createdBy = vClinicHistory.getCreatedBy();
		this.documentTypeId = vClinicHistory.getDocumentTypeId();
		this.sourceTypeId = vClinicHistory.getSourceTypeId();
		this.requestSourceId = vClinicHistory.getRequestSourceId();
		this.clinicalSpecialty = vClinicHistory.getClinicalSpecialty();
		this.institutionId = vClinicHistory.getInstitutionId();
		this.institution = vClinicHistory.getInstitution();
		this.requestSourceTypeId = vClinicHistory.getRequestSourceTypeId();
		this.startDate = vClinicHistory.getStartDate();
		this.endDate = vClinicHistory.getEndDate();
		this.patientAgePeriod = vClinicHistory.getPatientAgePeriod();
		this.encounterType = encounterType;
		this.documentType = documentType;
	}

	public List<ClinicalRecordBo> getClinicalRecords(){
		return Collections.emptyList();
	};

}
