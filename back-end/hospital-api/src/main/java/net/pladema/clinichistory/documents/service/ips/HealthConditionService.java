package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionNewConsultationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthHistoryConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface HealthConditionService {

    HealthConditionBo loadMainDiagnosis(PatientInfoBo patientInfo, Long documentId, Optional<HealthConditionBo> mainDiagnosis);

    List<DiagnosisBo> loadDiagnosis(PatientInfoBo patientInfo, Long documentId, List<DiagnosisBo> diagnosis);

    List<HealthHistoryConditionBo> loadPersonalHistories(PatientInfoBo patientInfo, Long documentId, List<HealthHistoryConditionBo> personalHistories);

    List<HealthHistoryConditionBo> loadFamilyHistories(PatientInfoBo patientInfo, Long documentId, List<HealthHistoryConditionBo> familyHistories);

    List<Integer> copyDiagnoses(List<Integer> diagnosesId);

    List<ProblemBo> loadProblems(PatientInfoBo patientInfo, Long documentId, List<ProblemBo> problems);

    HealthConditionNewConsultationBo getHealthCondition(Integer healthConditionId);

    Map<Integer, HealthConditionBo> getLastHealthCondition(Integer patientId, List<Integer> collect);
}
