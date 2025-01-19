package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEHospitalizationBo;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEPersonalHistoryBo;
import java.util.List;

public interface HCEHealthConditionsService {

    List<HCEHealthConditionBo> getSummaryProblems(Integer patientId);

	List<HCEHealthConditionBo> getSummaryProblemsByUser(Integer patientId, Integer userId);

    List<HCEHealthConditionBo> getFamilyHistories(Integer patientId);

    List<HCEHealthConditionBo> getChronicConditions(Integer institutionId, Integer patientId);
    
	List<HCEPersonalHistoryBo> getPersonalHistories(Integer patientId);

    List<HCEHealthConditionBo> getActiveProblems(Integer institutionId, Integer patientId);

    List<HCEHealthConditionBo> getSolvedProblems(Integer patientId);

    List<HCEHealthConditionBo> getProblemsAndChronicConditionsMarkedAsError(Integer patientId);

    List<HCEHospitalizationBo> getHospitalizationHistory(Integer patientId);

	List<HCEHospitalizationBo> getEmergencyCareHistory(Integer patientId);
}
