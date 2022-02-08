package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.*;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;

import java.time.LocalDateTime;
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

    private RiskFactorBo riskFactors;

    private List<ReasonBo> reasons = new ArrayList<>();

    private Integer clinicalSpecialtyId;

    private LocalDateTime performedDate;

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
