package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHospitalizationVo;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEPersonalHistoryVo;
import java.util.List;

public interface HCEHealthConditionRepository {

    List<HCEHealthConditionVo> getSummaryProblems(Integer patientId);

	List<HCEHealthConditionVo> getSummaryProblemsByUser(Integer patientId, Integer userId);

    List<HCEPersonalHistoryVo> getPersonalHistories(Integer patientId);

    List<HCEHealthConditionVo> getFamilyHistories(Integer patientId);

    List<HCEHospitalizationVo> getHospitalizationHistory(Integer patientId);

	List<HCEHospitalizationVo> getEmergencyCareHistory(Integer patientId);
}
