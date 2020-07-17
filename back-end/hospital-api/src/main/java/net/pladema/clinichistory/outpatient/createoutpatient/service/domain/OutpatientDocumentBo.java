package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.documents.service.InternmentDocument;
import net.pladema.clinichistory.ips.service.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutpatientDocumentBo implements InternmentDocument {

    private Long id;

    private boolean confirmed;

    private String reasonId;

    private String evolutionNote;

    private List<ProblemBo> problems = new ArrayList<>();

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
        return null;
    }
}
