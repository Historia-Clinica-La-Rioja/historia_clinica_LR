package ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo.port.DocumentInvolvedProfessionalStorage;
import ar.lamansys.sgh.clinichistory.application.reason.ReasonService;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompleteInformationForAllDocument {

    private final DocumentService documentService;

    private final ReasonService reasonService;

    private final DocumentInvolvedProfessionalStorage documentInvolvedProfessionalStorage;

    public void run(DocumentBo result) {

        Long documentId = result.getId();
        var healthCondition = documentService.getHealthConditionFromDocument(documentId);
        result.setDiagnosis(healthCondition.getDiagnosis());
        result.setMainDiagnosis(healthCondition.getMainDiagnosis());
        result.setPersonalHistories(healthCondition.getPersonalHistories());
        result.setFamilyHistories(healthCondition.getFamilyHistories());
        result.setProblems(healthCondition.getProblems());
        result.setOtherProblems(healthCondition.getOtherProblems());

        result.setReasons(reasonService.fetchFromDocumentId(documentId));
        result.setAllergies(documentService.getAllergyIntoleranceStateFromDocument(documentId));
        result.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(documentId));
        result.setProcedures(documentService.getProcedureStateFromDocument(documentId));
        result.setMedications(documentService.getMedicationStateFromDocument(documentId));
        result.setRiskFactors(documentService.getRiskFactorStateFromDocument(documentId));
        result.setDentalActions(documentService.getDentalActionsFromDocument(documentId));
        result.setExternalCause(documentService.getExternalCauseFromDocument(documentId));
        result.setObstetricEvent(documentService.getObstetricEventFromDocument(documentId));
        result.setOtherRiskFactors(documentService.getOtherRiskFactors(documentId));
        result.setConclusions(documentService.getConclusionsFromDocument(documentId));
        result.setInvolvedHealthcareProfessionalIds(documentInvolvedProfessionalStorage.fetchSignerInvolvedProfessionalIdsByDocumentId(documentId));
    }
}
