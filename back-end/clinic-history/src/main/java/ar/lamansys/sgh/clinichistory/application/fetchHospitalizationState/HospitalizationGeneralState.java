package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FamilyHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.Last2RiskFactorsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
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

    private List<PersonalHistoryBo> personalHistories;

    private List<FamilyHistoryBo> familyHistories;

    private List<MedicationBo> medications;

    private List<ImmunizationBo> immunizations;

    private List<AllergyConditionBo> allergies;

	private List<HealthConditionBo> otherProblems;

	private List<ProcedureBo> procedures;

    private AnthropometricDataBo anthropometricData;

    private Last2RiskFactorsBo riskFactors;

}
