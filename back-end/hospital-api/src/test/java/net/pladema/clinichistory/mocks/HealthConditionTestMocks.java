package net.pladema.clinichistory.mocks;

import net.pladema.clinichistory.documents.repository.entity.DocumentHealthCondition;
import net.pladema.clinichistory.ips.repository.entity.HealthCondition;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ProblemType;

public class HealthConditionTestMocks {

    private static HealthCondition createMinimunHealthCondition(Integer patientId, String code, String statusId, String verificationId) {
        HealthCondition result = new HealthCondition();
        result.setPatientId(patientId);
        result.setSctidCode(code);
        result.setStatusId(statusId);
        result.setVerificationStatusId(verificationId);
        result.setMain(false);
        return result;
    }

    public static HealthCondition createFamilyHistory(Integer patientId, String code, String statusId, String verificationId){
        HealthCondition result = createMinimunHealthCondition(patientId, code, statusId, verificationId);
        result.setProblemId(ProblemType.ANTECEDENTE);
        return result;
    }

    public static HealthCondition createPersonalHistory(Integer patientId, String code, String statusId, String verificationId){
        HealthCondition result = createMinimunHealthCondition(patientId, code, statusId, verificationId);
        result.setProblemId(ProblemType.PROBLEMA);
        return result;
    }

    public static HealthCondition createMainDiagnose(Integer patientId, String code, String statusId){
        HealthCondition result = createMinimunHealthCondition(patientId, code, statusId, ConditionVerificationStatus.CONFIRMED);
        result.setProblemId(ProblemType.DIAGNOSTICO);
        result.setMain(true);
        return result;
    }

    public static HealthCondition createDiagnose(Integer patientId, String code, String statusId, String verificationId){
        HealthCondition result = createMinimunHealthCondition(patientId, code, statusId, verificationId);
        result.setProblemId(ProblemType.DIAGNOSTICO);
        result.setMain(false);
        return result;
    }


    public static DocumentHealthCondition createHealthConditionDocument(Long documentId, Integer healthConditionId){
        DocumentHealthCondition result = new DocumentHealthCondition(documentId, healthConditionId);
        return result;
    }
}
