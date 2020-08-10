package net.pladema.clinichistory.mocks;

import net.pladema.clinichistory.documents.repository.entity.DocumentHealthCondition;
import net.pladema.clinichistory.ips.repository.entity.HealthCondition;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ProblemType;

public class HealthConditionTestMocks {

    private static HealthCondition createMinimumHealthCondition(Integer patientId, String code, String statusId, String verificationId) {
        HealthCondition result = new HealthCondition();
        result.setPatientId(patientId);
        result.setSctidCode(code);
        result.setStatusId(statusId);
        result.setVerificationStatusId(verificationId);
        result.setMain(false);
        return result;
    }

    public static HealthCondition createFamilyHistory(Integer patientId, String code, String statusId, String verificationId){
        HealthCondition result = createMinimumHealthCondition(patientId, code, statusId, verificationId);
        result.setProblemId(ProblemType.HISTORY);
        return result;
    }

    public static HealthCondition createPersonalHistory(Integer patientId, String code, String statusId, String verificationId){
        HealthCondition result = createMinimumHealthCondition(patientId, code, statusId, verificationId);
        result.setProblemId(ProblemType.PROBLEM);
        return result;
    }

    public static HealthCondition createChronicPersonalHistory(Integer patientId, String code, String verificationId){
        HealthCondition result = createMinimumHealthCondition(patientId, code, ConditionClinicalStatus.ACTIVE, verificationId);
        result.setProblemId(ProblemType.CHRONIC);
        return result;
    }

    public static HealthCondition createMainDiagnose(Integer patientId, String code, String statusId){
        HealthCondition result = createMinimumHealthCondition(patientId, code, statusId, ConditionVerificationStatus.CONFIRMED);
        result.setProblemId(ProblemType.DIAGNOSIS);
        result.setMain(true);
        return result;
    }

    public static HealthCondition createDiagnose(Integer patientId, String code, String statusId, String verificationId){
        HealthCondition result = createMinimumHealthCondition(patientId, code, statusId, verificationId);
        result.setProblemId(ProblemType.DIAGNOSIS);
        result.setMain(false);
        return result;
    }


    public static DocumentHealthCondition createHealthConditionDocument(Long documentId, Integer healthConditionId){
        DocumentHealthCondition result = new DocumentHealthCondition(documentId, healthConditionId);
        return result;
    }
}
