package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEHospitalizationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEPersonalHistoryBo;

import java.util.List;

public interface HCEHealthConditionsService {

    List<HCEPersonalHistoryBo> getActivePersonalHistories(Integer patientId);

    List<HCEPersonalHistoryBo> getFamilyHistories(Integer patientId);

    List<HCEPersonalHistoryBo> getChronicConditions(Integer patientId);

    List<HCEPersonalHistoryBo> getActiveProblems(Integer patientId);

    List<HCEPersonalHistoryBo> getSolvedProblems(Integer patientId);

    List<HCEHospitalizationBo> getHospitalizationHistory(Integer patientId);
}
