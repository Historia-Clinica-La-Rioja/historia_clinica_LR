package net.pladema.clinichistory.documents.service.generalstate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.hospitalization.service.domain.Last2VitalSignsBo;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class EncounterGeneralState implements Serializable {

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis;

    private List<HealthHistoryConditionBo> personalHistories;

    private List<HealthHistoryConditionBo> familyHistories;

    private List<MedicationBo> medications;

    private List<ImmunizationBo> immunizations;

    private List<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData;

    private Last2VitalSignsBo vitalSigns;

}
