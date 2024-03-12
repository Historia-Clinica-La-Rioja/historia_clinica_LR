package ar.lamansys.sgh.clinichistory.application.document;

import ar.lamansys.sgh.clinichistory.domain.document.DocumentDownloadDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnalgesicTechniqueBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticTechniqueBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ConclusionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DentalActionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentHealthcareProfessionalBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ExternalCauseBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ObstetricEventBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherRiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PostAnesthesiaStatusBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAllergyIntolerance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAnestheticTechnique;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentExternalCause;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthcareProfessional;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInmunization;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentMeasuringPoint;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentMedicamentionStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentObstetricEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyDiagnostic;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAnestheticSubstance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProsthesis;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentRiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentTriage;
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

	List<DocumentHealthcareProfessionalBo> getHealthcareProfessionalsFromDocument(Long documentId);

	Optional<String> getProsthesisDescriptionFromDocument(Long documentId);
    
    List<Long> getDocumentId(Integer sourceId, Short sourceTypeId);

    List<Long> getDocumentIdBySourceAndType(Integer sourceId, Short typeId);

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

	List<DentalActionBo> getDentalActionsFromDocument(Long id);

	DocumentExternalCause createDocumentExternalCause(Long documentId, Integer externalCauseId);

	ExternalCauseBo getExternalCauseFromDocument(Long documentId);

	DocumentObstetricEvent createDocumentObstetricEvent(Long documentId, Integer obstetricEventId);

	ObstetricEventBo getObstetricEventFromDocument(Long documentId);

	List<Long> getDocumentsIdsFromPatient(Integer patient);
	
	OtherRiskFactorBo getOtherRiskFactors(Long id);

	DocumentDownloadDataBo getDocumentDownloadDataByTriage(Integer triageId);

	DocumentDownloadDataBo getDocumentDownloadDataByAppointmentId(Integer appointmentId);

	DocumentTriage createDocumentTriage(Long documentId, Integer triageId);

    List<ConclusionBo> getConclusionsFromDocument(Long documentId);

	DocumentHealthcareProfessional createDocumentHealthcareProfessional(Long documentId, DocumentHealthcareProfessionalBo professional);

	DocumentProsthesis createDocumentProsthesis(Long documentId, String description);

    DocumentAnestheticSubstance createDocumentAnestheticSubstance(Long documentId, Integer substanceId);

    AnestheticHistoryBo getAnestheticHistoryStateFromDocument(Long documentId);

    List<AnestheticSubstanceBo> getAnestheticSubstancesStateFromDocument(Long documentId);

    ProcedureDescriptionBo getProcedureDescriptionStateFromDocument(Long documentId);

    List<AnalgesicTechniqueBo> getAnalgesicTechniquesStateFromDocument(Long documentId);

    DocumentAnestheticTechnique createDocumentAnestheticTechnique(Long documentId, Integer anestheticTechniqueId);

    List<AnestheticTechniqueBo> getAnestheticTechniquesStateFromDocument(Long documentId);

    DocumentMeasuringPoint createDocumentMeasuringPoint(Long documentId, Integer measuringPointId);

    List<MeasuringPointBo> getMeasuringPointStateFromDocument(Long documentId);

    PostAnesthesiaStatusBo getPostAnesthesiaStatusStateFromDocument(Long documentId);
}

