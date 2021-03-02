package net.pladema.clinichistory.documents.service;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.repository.entity.*;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.sgx.auditable.entity.Updateable;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    Optional<Document> findById(Long documentId);

    Document save(Document document);

    DocumentHealthCondition createDocumentHealthCondition(Long documentId, Integer healthConditionId);

    DocumentVitalSign createDocumentVitalSign(Long documentId, Integer observationVitalSignId);

    DocumentLab createDocumentLab(Long documentId, Integer observationLabId);

    DocumentAllergyIntolerance createDocumentAllergyIntolerance(Long documentId, Integer allergyIntoleranceId);

    DocumentInmunization createImmunization(Long documentId, Integer immunizationId);

    DocumentMedicamentionStatement createDocumentMedication(Long documentId, Integer medicationStatementId);

    DocumentProcedure createDocumentProcedure(Long documentId, Integer id);

    DocumentDiagnosticReport createDocumentDiagnosticReport(Long documentId, Integer diagnosticReportId);

    GeneralHealthConditionBo getHealthConditionFromDocument(Long documentId);

    List<ImmunizationBo> getImmunizationStateFromDocument(Long documentId);

    List<AllergyConditionBo> getAllergyIntoleranceStateFromDocument(Long documentId);

    List<MedicationBo> getMedicationStateFromDocument(Long documentId);

    AnthropometricDataBo getAnthropometricDataStateFromDocument(Long documentId);

    VitalSignBo getVitalSignStateFromDocument(Long documentId);

    List<Updateable> getUpdatableDocuments(Integer internmentEpisodeId);

    DocumentMedicamentionStatement getDocumentFromMedication(Integer mid);

    DocumentDiagnosticReport getDocumentFromDiagnosticReport(Integer drid);

    Long getDocumentId(Integer sourceId, Short sourceTypeId);

    void deleteHealthConditionHistory(Long documentId);

    void deleteAllergiesHistory(Long documentId);

    void deleteImmunizationsHistory(Long documentId);

    void deleteMedicationsHistory(Long documentId);

    void deleteObservationsVitalSignsHistory(Long documentId);

    void deleteObservationsLabHistory(Long documentId);
}

