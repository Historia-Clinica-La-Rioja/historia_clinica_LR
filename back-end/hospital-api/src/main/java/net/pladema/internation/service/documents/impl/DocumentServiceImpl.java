package net.pladema.internation.service.documents.impl;

import net.pladema.internation.repository.core.DocumentHealthConditionRepository;
import net.pladema.internation.repository.core.entity.DocumentHealthCondition;
import net.pladema.internation.repository.core.entity.DocumentHealthConditionPK;
import net.pladema.internation.service.documents.DocumentService;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    public DocumentServiceImpl(DocumentHealthConditionRepository documentHealthConditionRepository){
        this.documentHealthConditionRepository = documentHealthConditionRepository;
    }

    @Override
    public void createHealthConditionIndex(Long documentId, Integer healthConditionId) {

        DocumentHealthConditionPK pk = new DocumentHealthConditionPK(documentId, healthConditionId);
        DocumentHealthCondition document = new DocumentHealthCondition();
        document.setPk(pk);
        documentHealthConditionRepository.save(document);
    }
}
