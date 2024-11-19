package ar.lamansys.sgh.clinichistory.domain.document.impl;


import ar.lamansys.sgh.clinichistory.domain.document.visitor.DocumentVisitor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class MedicationRequestBo implements IDocumentBo {

    private Long id;

    private Integer medicationRequestId;

    private PatientInfoBo patientInfo;

    private Integer medicalCoverageId;

    private Integer doctorId;

    private Integer encounterId;

    private Integer institutionId;

    private List<MedicationBo> medications = new ArrayList<>();

    private boolean hasRecipe = false;

    private LocalDate requestDate = LocalDate.now();

	private Boolean isPostDated;

	private Integer repetitions;

	private Integer clinicalSpecialtyId;

	private Boolean isArchived;

	private UUID uuid;

    private Map<String, Object> contextMap;

	public short getDocumentType() {
        return DocumentType.RECIPE;
    }

	@Override
	public LocalDateTime getPerformedDate() {
		return requestDate.atStartOfDay();
	}

    @Override
    public Short getDocumentSource() {
        return SourceType.RECIPE;
    }

    @Override
    public Integer getPatientId() {
        if (patientInfo == null)
            return null;
        return patientInfo.getId();
    }

	public void setPatientId(Integer patientId) {
		if (patientInfo == null)
			patientInfo = new PatientInfoBo(patientId);
		else
			patientInfo.setId(patientId);
	}

	public MedicationRequestBo(MedicationRequestBo medicationRequestBo, Integer key, LocalDate value) {
		this.id = medicationRequestBo.getId();
		this.medicationRequestId = medicationRequestBo.getMedicationRequestId();
		this.patientInfo = medicationRequestBo.getPatientInfo();
		this.medicalCoverageId = medicationRequestBo.getMedicalCoverageId();
		this.doctorId = medicationRequestBo.getDoctorId();
		this.institutionId = medicationRequestBo.getInstitutionId();
		this.medications = medicationRequestBo.getMedications();
		this.hasRecipe = medicationRequestBo.isHasRecipe();
		this.isPostDated = medicationRequestBo.getIsPostDated();
		this.repetitions = medicationRequestBo.getRepetitions();
		this.clinicalSpecialtyId = medicationRequestBo.getClinicalSpecialtyId();
		this.isArchived = medicationRequestBo.getIsArchived();
		this.uuid = medicationRequestBo.getUuid();
		this.encounterId = key;
		this.requestDate = value;
	}

	@Override
	public void accept(DocumentVisitor documentVisitor) {
		documentVisitor.visitMedicationRequest(this);
	}

}
