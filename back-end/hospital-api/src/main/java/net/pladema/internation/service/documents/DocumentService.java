package net.pladema.internation.service.documents;

import net.pladema.internation.repository.documents.entity.Document;
import net.pladema.internation.repository.documents.entity.DocumentInmunization;
import net.pladema.internation.repository.documents.entity.DocumentLab;
import net.pladema.internation.repository.documents.entity.DocumentVitalSign;
import net.pladema.internation.service.ips.domain.*;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    Optional<Document> findById(Long documentId);

    Document save(Document document);

    void createDocumentHealthCondition(Long documentId, Integer healthConditionId);

    DocumentVitalSign createDocumentVitalSign(Long documentId, Integer observationVitalSignId);

    DocumentLab createDocumentLab(Long documentId, Integer observationLabId);

    void createDocumentAllergyIntolerance(Long documentId, Integer allergyIntoleranceId);

    DocumentInmunization createInmunization(Long documentId, Integer inmunizationId);
    
    void createDocumentMedication(Long documentId, Integer medicationStatementId);

    GeneralHealthConditionBo getHealthConditionFromDocument(Long documentId);

    List<InmunizationBo> getInmunizationStateFromDocument(Long documentId);

    List<AllergyConditionBo> getAllergyIntoleranceStateFromDocument(Long documentId);

    List<MedicationBo> getMedicationStateFromDocument(Long documentId);

    AnthropometricDataBo getAnthropometricDataStateFromDocument(Long documentId);

    VitalSignBo getVitalSignStateFromDocument(Long documentId);

    void deleteHealthConditionHistory(Long documentId);

    void deleteAllergiesHistory(Long documentId);

    void deleteInmunizationsHistory(Long documentId);

    void deleteMedicationsHistory(Long documentId);

    void deleteObservationsVitalSignsHistory(Long documentId);

    void deleteObservationsLabHistory(Long documentId);

}

