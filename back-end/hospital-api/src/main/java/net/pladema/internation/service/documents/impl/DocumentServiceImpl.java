package net.pladema.internation.service.documents.impl;

import net.pladema.internation.repository.core.*;
import net.pladema.internation.repository.core.entity.*;
import net.pladema.internation.repository.ips.generalstate.*;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.ips.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    public static final String OUTPUT = "Output -> {}";

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);
    
    private static final String LOGGING_DOCUMENT_ID = "Input parameters -> documentId {}";
    
    private static final String LOGGING_DELETE_SUCCESS = "Delete success";

    private final DocumentRepository documentRepository;

    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    private final DocumentInmunizationRepository documentInmunizationRepository;

    private final DocumentVitalSignRepository documentVitalSignRepository;

    private final DocumentLabRepository documentLabRepository;

    private final DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository;

    private final DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository,
                               DocumentHealthConditionRepository documentHealthConditionRepository,
                               DocumentInmunizationRepository documentInmunizationRepository,
                               DocumentVitalSignRepository documentVitalSignRepository,
                               DocumentLabRepository documentLabRepository,
                               DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository,
                               DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository) {
        this.documentRepository = documentRepository;
        this.documentHealthConditionRepository = documentHealthConditionRepository;
        this.documentInmunizationRepository = documentInmunizationRepository;
        this.documentVitalSignRepository = documentVitalSignRepository;
        this.documentLabRepository = documentLabRepository;
        this.documentAllergyIntoleranceRepository = documentAllergyIntoleranceRepository;
        this.documentMedicamentionStatementRepository = documentMedicamentionStatementRepository;
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
    public void createDocumentHealthCondition(Long documentId, Integer healthConditionId) {
        LOG.debug("Input parameters -> documentId {}, healthConditionId {}", documentId, healthConditionId);
        DocumentHealthCondition document = new DocumentHealthCondition(documentId, healthConditionId);
        documentHealthConditionRepository.save(document);
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
    public void createDocumentAllergyIntolerance(Long documentId, Integer allergyIntoleranceId) {
        LOG.debug("Input parameters -> documentId {}, allergyIntoleranceId {}", documentId, allergyIntoleranceId);
        DocumentAllergyIntolerance document = new DocumentAllergyIntolerance(documentId, allergyIntoleranceId);
        documentAllergyIntoleranceRepository.save(document);
    }

    @Override
    public DocumentInmunization createInmunization(Long documentId, Integer inmunizationId) {
        LOG.debug("Input parameters -> documentId {}, inmunizationId {}", documentId, inmunizationId);
        DocumentInmunization result = new DocumentInmunization(documentId, inmunizationId);
        result = documentInmunizationRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public void createDocumentMedication(Long documentId, Integer medicationStatementId) {
        LOG.debug("Input parameters -> documentId {}, medicationStatementId {}", documentId, medicationStatementId);
        DocumentMedicamentionStatement document = new DocumentMedicamentionStatement(documentId, medicationStatementId);
        documentMedicamentionStatementRepository.save(document);
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
    public List<InmunizationBo> getInmunizationStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<InmunizationVo> resultQuery = documentInmunizationRepository.getInmunizationStateFromDocument(documentId);
        List<InmunizationBo> result = resultQuery.stream().map(InmunizationBo::new).collect(Collectors.toList());
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
    public void deleteHealthConditionHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        // TODO
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

    @Override
    public void deleteAllergiesHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        // TODO
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

    @Override
    public void deleteInmunizationsHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        // TODO
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

    @Override
    public void deleteMedicationsHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        // TODO
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

    @Override
    public void deleteObservationsVitalSignsHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        // TODO
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }

    @Override
    public void deleteObservationsLabHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        // TODO
        LOG.debug(LOGGING_DELETE_SUCCESS);
    }


}
