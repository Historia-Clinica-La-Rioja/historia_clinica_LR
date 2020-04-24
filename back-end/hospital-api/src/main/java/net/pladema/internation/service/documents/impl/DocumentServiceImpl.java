package net.pladema.internation.service.documents.impl;

import net.pladema.internation.repository.core.*;
import net.pladema.internation.repository.core.DocumentHealthConditionRepository;
import net.pladema.internation.repository.core.DocumentInmunizationRepository;
import net.pladema.internation.repository.core.DocumentLabRepository;
import net.pladema.internation.repository.core.DocumentVitalSignRepository;
import net.pladema.internation.repository.core.entity.*;
import net.pladema.internation.service.documents.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    public static final String OUTPUT = "Output -> {}";

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

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
    public Document create(Document document) {
        return documentRepository.save(document);
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
}
