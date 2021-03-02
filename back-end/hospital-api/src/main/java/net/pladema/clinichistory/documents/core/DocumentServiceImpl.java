package net.pladema.clinichistory.documents.core;

import net.pladema.clinichistory.documents.repository.*;
import net.pladema.clinichistory.documents.repository.entity.*;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.repository.generalstate.domain.*;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.sgx.auditable.entity.Updateable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class  DocumentServiceImpl implements DocumentService {

    public static final String OUTPUT = "Output -> {}";

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);
    
    private static final String LOGGING_DOCUMENT_ID = "Input parameters -> documentId {}";
    
    private static final String LOGGING_DELETE_SUCCESS = "Delete success";

    private final DocumentRepository documentRepository;

    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    private final DocumentImmunizationRepository documentImmunizationRepository;

    private final DocumentProcedureRepository documentProcedureRepository;

    private final DocumentVitalSignRepository documentVitalSignRepository;

    private final DocumentLabRepository documentLabRepository;

    private final DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository;

    private final DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository;

    private final DocumentDiagnosticReportRepository documentDiagnosticReportRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository,
                               DocumentHealthConditionRepository documentHealthConditionRepository,
                               DocumentImmunizationRepository documentImmunizationRepository,
                               DocumentProcedureRepository documentProcedureRepository,
                               DocumentVitalSignRepository documentVitalSignRepository,
                               DocumentLabRepository documentLabRepository,
                               DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository,
                               DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository,
                               DocumentDiagnosticReportRepository documentDiagnosticReportRepository) {
        this.documentRepository = documentRepository;
        this.documentHealthConditionRepository = documentHealthConditionRepository;
        this.documentImmunizationRepository = documentImmunizationRepository;
        this.documentProcedureRepository = documentProcedureRepository;
        this.documentVitalSignRepository = documentVitalSignRepository;
        this.documentLabRepository = documentLabRepository;
        this.documentAllergyIntoleranceRepository = documentAllergyIntoleranceRepository;
        this.documentMedicamentionStatementRepository = documentMedicamentionStatementRepository;
        this.documentDiagnosticReportRepository = documentDiagnosticReportRepository;
    }

    @Override
    public Optional<Document> findById(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        Optional<Document> result = documentRepository.findById(documentId);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Document save(Document document) {
        LOG.debug("Input parameters -> document {}", document);
        document = documentRepository.save(document);
        LOG.debug(OUTPUT, document);
        return document;
    }

    @Override
    public DocumentHealthCondition createDocumentHealthCondition(Long documentId, Integer healthConditionId) {
        LOG.debug("Input parameters -> documentId {}, healthConditionId {}", documentId, healthConditionId);
        DocumentHealthCondition result = new DocumentHealthCondition(documentId, healthConditionId);
        result = documentHealthConditionRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentVitalSign createDocumentVitalSign(Long documentId, Integer observationVitalSignId) {
        LOG.debug("Input parameters -> documentId {}, observationVitalSignId {}", documentId, observationVitalSignId);
        DocumentVitalSign result = new DocumentVitalSign(documentId, observationVitalSignId);
        result = documentVitalSignRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentLab createDocumentLab(Long documentId, Integer observationLabId) {
        LOG.debug("Input parameters -> documentId {}, observationLabId {}", documentId, observationLabId);
        DocumentLab result = new DocumentLab(documentId, observationLabId);
        result = documentLabRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentAllergyIntolerance createDocumentAllergyIntolerance(Long documentId, Integer allergyIntoleranceId) {
        LOG.debug("Input parameters -> documentId {}, allergyIntoleranceId {}", documentId, allergyIntoleranceId);
        DocumentAllergyIntolerance result = new DocumentAllergyIntolerance(documentId, allergyIntoleranceId);
        result = documentAllergyIntoleranceRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentInmunization createImmunization(Long documentId, Integer immunizationId) {
        LOG.debug("Input parameters -> documentId {}, immunizationId {}", documentId, immunizationId);
        DocumentInmunization result = new DocumentInmunization(documentId, immunizationId);
        result = documentImmunizationRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentMedicamentionStatement createDocumentMedication(Long documentId, Integer medicationStatementId) {
        LOG.debug("Input parameters -> documentId {}, medicationStatementId {}", documentId, medicationStatementId);
        DocumentMedicamentionStatement result = new DocumentMedicamentionStatement(documentId, medicationStatementId);
        result = documentMedicamentionStatementRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentProcedure createDocumentProcedure(Long documentId, Integer procedureId) {
        LOG.debug("Input parameters -> documentId {}, procedureId {}", documentId, procedureId);
        DocumentProcedure result = new DocumentProcedure(documentId, procedureId);
        result = documentProcedureRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;

    }

    @Override
    public DocumentDiagnosticReport createDocumentDiagnosticReport(Long documentId, Integer diagnosticReportId) {
        LOG.debug("Input parameters -> documentId {}, procedureId {}", documentId, diagnosticReportId);
        DocumentDiagnosticReport result = new DocumentDiagnosticReport(documentId, diagnosticReportId);
        result = documentDiagnosticReportRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public GeneralHealthConditionBo getHealthConditionFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<HealthConditionVo> resultQuery = documentHealthConditionRepository.getHealthConditionFromDocument(documentId);
        GeneralHealthConditionBo result = new GeneralHealthConditionBo(resultQuery);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<ImmunizationBo> getImmunizationStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<ImmunizationVo> resultQuery = documentImmunizationRepository.getImmunizationStateFromDocument(documentId);
        List<ImmunizationBo> result = resultQuery.stream().map(ImmunizationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<AllergyConditionBo> getAllergyIntoleranceStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<AllergyConditionVo> resultQuery = documentAllergyIntoleranceRepository.getAllergyIntoleranceStateFromDocument(documentId);
        List<AllergyConditionBo> result = resultQuery.stream().map(AllergyConditionBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<MedicationBo> getMedicationStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<MedicationVo> resultQuery = documentMedicamentionStatementRepository.getMedicationStateFromDocument(documentId);
        List<MedicationBo> result = resultQuery.stream().map(MedicationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public AnthropometricDataBo getAnthropometricDataStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<ClinicalObservationVo> clinicalObservationVos = documentVitalSignRepository.getVitalSignStateFromDocument(documentId);
        clinicalObservationVos.addAll(documentLabRepository.getLabStateFromDocument(documentId));
        MapClinicalObservationVo resultQuery = new MapClinicalObservationVo(clinicalObservationVos);
        AnthropometricDataBo result = resultQuery.getLastAnthropometricData().orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public VitalSignBo getVitalSignStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        MapClinicalObservationVo resultQuery = new MapClinicalObservationVo(documentVitalSignRepository.getVitalSignStateFromDocument(documentId));
        VitalSignBo result = resultQuery.getLastVitalSigns().orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<Updateable> getUpdatableDocuments(Integer internmentEpisodeId) {
        return documentRepository.getUpdatablesDocuments(internmentEpisodeId);
    }

    @Override
    public DocumentMedicamentionStatement getDocumentFromMedication(Integer mid) {
        LOG.debug("medicationId -> {}", mid);
        DocumentMedicamentionStatement result = documentMedicamentionStatementRepository.findByMedicationId(mid);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentDiagnosticReport getDocumentFromDiagnosticReport(Integer drid) {
        LOG.debug("DiagnosticReportId -> {}", drid);
        DocumentDiagnosticReport result = documentDiagnosticReportRepository.findByDiagnosticReportId(drid);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Long getDocumentId(Integer sourceId, Short sourceTypeId) {
        return documentRepository.findBySourceIdAndSourceTypeId(sourceId,sourceTypeId);
    }

    @Override
    public void deleteHealthConditionHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

    @Override
    public void deleteAllergiesHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

    @Override
    public void deleteImmunizationsHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

    @Override
    public void deleteMedicationsHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

    @Override
    public void deleteObservationsVitalSignsHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

    @Override
    public void deleteObservationsLabHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

}
