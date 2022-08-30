package ar.lamansys.sgh.clinichistory.application.document;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAllergyIntolerance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInmunization;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentMedicamentionStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyDiagnostic;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentRiskFactor;
import ar.lamansys.sgx.shared.auditable.entity.Updateable;

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

    void deleteProceduresHistory(Long documentId);

    void deleteById(Long documentId, String documentStatus);

    void updateDocumentModificationReason(Long documentId, String reason);

	Short getSourceType(Long documentId);
}

