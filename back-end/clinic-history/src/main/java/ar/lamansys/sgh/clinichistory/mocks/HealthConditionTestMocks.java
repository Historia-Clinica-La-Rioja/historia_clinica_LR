package ar.lamansys.sgh.clinichistory.mocks;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;

public class HealthConditionTestMocks {

    private static HealthCondition createMinimumHealthCondition(Integer patientId, Integer snomedId, String statusId, String verificationId) {
        HealthCondition result = new HealthCondition();
        result.setPatientId(patientId);
        result.setSnomedId(snomedId);
        result.setStatusId(statusId);
        result.setVerificationStatusId(verificationId);
        result.setMain(false);
        return result;
    }

    public static HealthCondition createFamilyHistory(Integer patientId, Integer snomedId, String statusId, String verificationId){
        HealthCondition result = createMinimumHealthCondition(patientId, snomedId, statusId, verificationId);
        result.setProblemId(ProblemType.HISTORY);
        return result;
    }

    public static HealthCondition createPersonalHistory(Integer patientId, Integer snomedId, String statusId, String verificationId){
        HealthCondition result = createMinimumHealthCondition(patientId, snomedId, statusId, verificationId);
        result.setProblemId(ProblemType.PROBLEM);
        return result;
    }

    public static HealthCondition createPersonalHistoryWithCie10Codes(Integer patientId, Integer snomedId, String statusId, String verificationId, String cie10Codes){
        HealthCondition result = createMinimumHealthCondition(patientId, snomedId, statusId, verificationId);
        result.setProblemId(ProblemType.PROBLEM);
        result.setCie10Codes(cie10Codes);
        return result;
    }

    public static HealthCondition createChronicPersonalHistory(Integer patientId, Integer snomedId, String verificationId){
        HealthCondition result = createMinimumHealthCondition(patientId, snomedId, ConditionClinicalStatus.ACTIVE, verificationId);
        result.setProblemId(ProblemType.CHRONIC);
        return result;
    }

    public static HealthCondition createMainDiagnose(Integer patientId, Integer snomedId, String statusId){
        HealthCondition result = createMinimumHealthCondition(patientId, snomedId, statusId, ConditionVerificationStatus.CONFIRMED);
        result.setProblemId(ProblemType.DIAGNOSIS);
        result.setMain(true);
        return result;
    }

    public static HealthCondition createDiagnose(Integer patientId, Integer snomedId, String statusId, String verificationId){
        HealthCondition result = createMinimumHealthCondition(patientId, snomedId, statusId, verificationId);
        result.setProblemId(ProblemType.DIAGNOSIS);
        result.setMain(false);
        return result;
    }


    public static DocumentHealthCondition createHealthConditionDocument(Long documentId, Integer healthConditionId){
        DocumentHealthCondition result = new DocumentHealthCondition(documentId, healthConditionId);
        return result;
    }
}
