package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.hospitalization.service.domain.ClinicalSpecialtyBo;
import net.pladema.clinichistory.documents.service.ips.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutpatientDocumentBo implements Document {

    private Long id;

    private boolean confirmed = true;

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
    public HealthConditionBo getMainDiagnosis() {
        return null;
    }

    @Override
    public List<DiagnosisBo> getDiagnosis() {
        return Collections.emptyList();
    }

    @Override
    public List<HealthHistoryConditionBo> getPersonalHistories() {
        return Collections.emptyList();
    }

    @Override
    public DocumentObservationsBo getNotes() {
        if (evolutionNote == null)
            return null;
        DocumentObservationsBo notes = new DocumentObservationsBo();
        notes.setOtherNote(evolutionNote);
        return notes;
    }

    @Override
    public short getDocumentType() {
        return 0;
    }

    @Override
    public Integer getEncounterId() {
        return null;
    }

    @Override
    public Short getDocumentSource() {
        return null;
    }

    @Override
    public String getDocumentStatusId() {
        return null;
    }

    @Override
    public Integer getPatientId() {
        return null;
    }

}
