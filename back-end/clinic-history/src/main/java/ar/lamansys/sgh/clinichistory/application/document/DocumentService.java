package ar.lamansys.sgh.clinichistory.application.document;

import ar.lamansys.sgh.clinichistory.domain.ips.*;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.*;
import ar.lamansys.sgx.shared.auditable.entity.Updateable;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    Optional<Document> findById(Long documentId);

    Document save(Document document);

    DocumentHealthCondition createDocumentHealthCondition(Long documentId, Integer healthConditionId);

    DocumentRiskFactor createDocumentRiskFactor(Long documentId, Integer observationRiskFactorsId);

    DocumentLab createDocumentLab(Long documentId, Integer observationLabId);

    DocumentAllergyIntolerance createDocumentAllergyIntolerance(Long documentId, Integer allergyIntoleranceId);

    DocumentInmunization createImmunization(Long documentId, Integer immunizationId);

    DocumentMedicamentionStatement createDocumentMedication(Long documentId, Integer medicationStatementId);

    DocumentProcedure createDocumentProcedure(Long documentId, Integer id);

    DocumentDiagnosticReport createDocumentDiagnosticReport(Long documentId, Integer diagnosticReportId);

    DocumentOdontologyProcedure createDocumentOdontologyProcedure(Long documentId, Integer odontologyProcedureId);

    DocumentOdontologyDiagnostic createDocumentOdontologyDiagnostic(Long documentId, Integer id);

    GeneralHealthConditionBo getHealthConditionFromDocument(Long documentId);

    List<ImmunizationBo> getImmunizationStateFromDocument(Long documentId);

    List<AllergyConditionBo> getAllergyIntoleranceStateFromDocument(Long documentId);

    List<MedicationBo> getMedicationStateFromDocument(Long documentId);

    AnthropometricDataBo getAnthropometricDataStateFromDocument(Long documentId);

    RiskFactorBo getRiskFactorStateFromDocument(Long documentId);

	List<ProcedureBo> getProcedureStateFromDocument(Long documentId);

	List<Updateable> getUpdatableDocuments(Integer internmentEpisodeId);

    DocumentMedicamentionStatement getDocumentFromMedication(Integer mid);

    DocumentDiagnosticReport getDocumentFromDiagnosticReport(Integer drid);

    List<Long> getDocumentId(Integer sourceId, Short sourceTypeId);

    void deleteHealthConditionHistory(Long documentId);

    void deleteAllergiesHistory(Long documentId);

    void deleteImmunizationsHistory(Long documentId);

    void deleteMedicationsHistory(Long documentId);

    void deleteObservationsRiskFactorsHistory(Long documentId);

    void deleteObservationsLabHistory(Long documentId);
}

