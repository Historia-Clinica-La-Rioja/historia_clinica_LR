package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.ips.service.domain.*;

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

    private List<ProblemBo> problems;

    private List<ProcedureBo> procedures;

    private List<HealthHistoryConditionBo> familyHistories;

    private List<MedicationBo> medications;

    private List<InmunizationBo> inmunizations;

    private List<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData;

    private VitalSignBo vitalSigns;

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
}
