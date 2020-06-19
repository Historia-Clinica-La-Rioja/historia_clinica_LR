package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.service.domain.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class InternmentGeneralState implements Serializable {

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis;

    private List<HealthHistoryConditionBo> personalHistories;

    private List<HealthHistoryConditionBo> familyHistories;

    private List<MedicationBo> medications;

    private List<InmunizationBo> inmunizations;

    private List<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData;

    private Last2VitalSignsBo vitalSigns;

}
