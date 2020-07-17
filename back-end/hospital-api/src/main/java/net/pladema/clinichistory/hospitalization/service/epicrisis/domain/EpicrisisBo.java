package net.pladema.clinichistory.hospitalization.service.epicrisis.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.service.domain.*;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class EpicrisisBo implements Document {

    private Long id;

    private boolean confirmed;

    private DocumentObservationsBo notes;

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis;

    private List<HealthHistoryConditionBo> personalHistories;

    private List<HealthHistoryConditionBo> familyHistories;

    private List<MedicationBo> medications;

    private List<InmunizationBo> inmunizations;

    private List<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData;

    private VitalSignBo vitalSigns;

    public String getDocumentStatusId(){
        return confirmed ? DocumentStatus.FINAL : DocumentStatus.DRAFT;
    }

    @Override
    public List<ProblemBo> getProblems() {
        return Collections.emptyList();
    }
}
