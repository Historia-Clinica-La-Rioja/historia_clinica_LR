package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.domain.DiagnosisBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionNewConsultationBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthHistoryConditionBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;
import net.pladema.patient.controller.dto.BasicPatientDto;

import java.util.List;
import java.util.Optional;

public interface HealthConditionService {

    HealthConditionBo loadMainDiagnosis(PatientInfoBo patientInfo, Long documentId, Optional<HealthConditionBo> mainDiagnosis);

    List<DiagnosisBo> loadDiagnosis(PatientInfoBo patientInfo, Long documentId, List<DiagnosisBo> diagnosis);

    List<HealthHistoryConditionBo> loadPersonalHistories(PatientInfoBo patientInfo, Long documentId, List<HealthHistoryConditionBo> personalHistories);

    List<HealthHistoryConditionBo> loadFamilyHistories(PatientInfoBo patientInfo, Long documentId, List<HealthHistoryConditionBo> familyHistories);

    List<Integer> copyDiagnoses(List<Integer> diagnosesId);

    List<ProblemBo> loadProblems(PatientInfoBo patientInfo, Long documentId, List<ProblemBo> problems);

    HealthConditionNewConsultationBo getHealthCondition(Integer healthConditionId);

}
