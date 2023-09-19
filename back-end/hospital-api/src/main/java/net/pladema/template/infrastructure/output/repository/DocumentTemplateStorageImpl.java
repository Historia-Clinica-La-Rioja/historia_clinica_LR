package net.pladema.template.infrastructure.output.repository;

import lombok.RequiredArgsConstructor;
import net.pladema.template.application.port.DocumentTemplateStorage;
import net.pladema.template.domain.DocumentTemplateBo;
import net.pladema.template.infrastructure.output.repository.entity.DocumentTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentTemplateStorageImpl implements DocumentTemplateStorage {

    private final DocumentTemplateRepository documentTemplateRepository;

    @Override
    public void save(DocumentTemplateBo documentTemplateBo, Long fileId) {
        DocumentTemplate documentTemplate = mapTo(documentTemplateBo);
        documentTemplate.setFileId(fileId);
        documentTemplateRepository.save(documentTemplate);
    }

    @Override
    public boolean exists(Integer userId, String name, Short typeId) {
        return documentTemplateRepository.exists(userId, name, typeId).isPresent();
    }

    private DocumentTemplate mapTo(DocumentTemplateBo documentTemplateBo) {
        DocumentTemplate documentTemplate = new DocumentTemplate();
        documentTemplate.setName(documentTemplateBo.getName());
        documentTemplate.setUserId(documentTemplateBo.getUserId());
        documentTemplate.setTypeId(documentTemplateBo.getTypeId());
        documentTemplate.setInstitutionId(documentTemplateBo.getInstitutionId());
        return documentTemplate;
    }
}
