package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.ips.domain.DiagnosisBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionNewConsultationBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthHistoryConditionBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;

import java.util.List;
import java.util.Optional;

public interface HealthConditionService {

    HealthConditionBo loadMainDiagnosis(Integer patientId, Long documentId, Optional<HealthConditionBo> mainDiagnosis);

    List<DiagnosisBo> loadDiagnosis(Integer patientId, Long documentId, List<DiagnosisBo> diagnosis);

    List<HealthHistoryConditionBo> loadPersonalHistories(Integer patientId, Long documentId, List<HealthHistoryConditionBo> personalHistories);

    List<HealthHistoryConditionBo> loadFamilyHistories(Integer patientId, Long documentId, List<HealthHistoryConditionBo> familyHistories);

    List<Integer> copyDiagnoses(List<Integer> diagnosesId);

    List<ProblemBo> loadProblems(Integer patientId, Long documentId, List<ProblemBo> problems);

    HealthConditionNewConsultationBo getHealthCondition(Integer healthConditionId);

}
