package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.domain.ips.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class HospitalizationGeneralState implements Serializable {

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis;

    private List<HealthHistoryConditionBo> personalHistories;

    private List<HealthHistoryConditionBo> familyHistories;

    private List<MedicationBo> medications;

    private List<ImmunizationBo> immunizations;

    private List<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData;

    private Last2RiskFactorsBo riskFactors;

}
