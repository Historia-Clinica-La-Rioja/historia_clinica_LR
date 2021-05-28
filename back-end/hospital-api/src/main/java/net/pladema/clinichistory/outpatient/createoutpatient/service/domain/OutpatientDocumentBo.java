package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.*;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.documents.service.IDocumentBo;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.domain.ClinicalSpecialtyBo;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutpatientDocumentBo implements IDocumentBo {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private Integer encounterId;

    private Integer institutionId;

    private Integer doctorId;

    private String reasonId;

    private String evolutionNote;

    private List<ProblemBo> problems = new ArrayList<>();

    private List<ProcedureBo> procedures = new ArrayList<>();

    private List<HealthHistoryConditionBo> familyHistories = new ArrayList<>();

    private List<MedicationBo> medications = new ArrayList<>();

    private List<ImmunizationBo> immunizations = new ArrayList<>();

    private List<AllergyConditionBo> allergies = new ArrayList<>();

    private AnthropometricDataBo anthropometricData ;

    private VitalSignBo vitalSigns;

    private List<ReasonBo> reasons = new ArrayList<>();

    private ClinicalSpecialtyBo clinicalSpecialty;

    @Override
    public DocumentObservationsBo getNotes() {
        if (evolutionNote == null)
            return null;
        DocumentObservationsBo notes = new DocumentObservationsBo();
        notes.setOtherNote(evolutionNote);
        return notes;
    }

    @Override
    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return patientId;
    }

    @Override
    public short getDocumentType() {
        return DocumentType.OUTPATIENT;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.OUTPATIENT;
    }

}
