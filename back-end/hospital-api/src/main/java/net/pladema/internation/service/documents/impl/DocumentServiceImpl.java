package net.pladema.internation.service.documents.impl;

import net.pladema.internation.repository.core.DocumentHealthConditionRepository;
import net.pladema.internation.repository.core.DocumentLabRepository;
import net.pladema.internation.repository.core.DocumentVitalSignRepository;
import net.pladema.internation.repository.core.entity.DocumentHealthCondition;
import net.pladema.internation.repository.core.entity.DocumentHealthConditionPK;
import net.pladema.internation.repository.core.entity.DocumentLab;
import net.pladema.internation.repository.core.entity.DocumentVitalSign;
import net.pladema.internation.service.documents.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    public static final String OUTPUT = "Output -> {}";

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    private final DocumentVitalSignRepository documentVitalSignRepository;

    private final DocumentLabRepository documentLabRepository;

    public DocumentServiceImpl(DocumentHealthConditionRepository documentHealthConditionRepository,
                               DocumentVitalSignRepository documentVitalSignRepository,
                               DocumentLabRepository documentLabRepository) {
        this.documentHealthConditionRepository = documentHealthConditionRepository;
        this.documentVitalSignRepository = documentVitalSignRepository;
        this.documentLabRepository = documentLabRepository;
    }

    @Override
    public void createHealthConditionIndex(Long documentId, Integer healthConditionId) {
        LOG.debug("Input parameters -> documentId {}, healthConditionId {}", documentId, healthConditionId);
        DocumentHealthConditionPK pk = new DocumentHealthConditionPK(documentId, healthConditionId);
        DocumentHealthCondition document = new DocumentHealthCondition();
        document.setPk(pk);
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
}
