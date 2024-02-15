package net.pladema.template.infrastructure.output.repository;

import lombok.RequiredArgsConstructor;
import net.pladema.template.application.port.DocumentTemplateStorage;
import net.pladema.template.domain.DocumentTemplateBo;
import net.pladema.template.infrastructure.output.repository.entity.DocumentTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<DocumentTemplateBo> get(Integer userId, Short typeId) {
        return documentTemplateRepository.getTemplates(userId, typeId).stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Long> getFileId(Long id) {
        return documentTemplateRepository.getFileId(id);
    }

    @Override
    public void delete(Long id) {
        documentTemplateRepository.deleteById(id);
    }

    private DocumentTemplate mapTo(DocumentTemplateBo documentTemplateBo) {
        DocumentTemplate documentTemplate = new DocumentTemplate();
        documentTemplate.setName(documentTemplateBo.getName());
        documentTemplate.setUserId(documentTemplateBo.getUserId());
        documentTemplate.setTypeId(documentTemplateBo.getTypeId());
        documentTemplate.setInstitutionId(documentTemplateBo.getInstitutionId());
        documentTemplate.setDeleted(false);
        return documentTemplate;
    }

    private DocumentTemplateBo mapTo(DocumentTemplate documentTemplate) {
        DocumentTemplateBo documentTemplateBo = new DocumentTemplateBo();
        documentTemplateBo.setId(documentTemplate.getId());
        documentTemplateBo.setName(documentTemplate.getName());
        documentTemplateBo.setUserId(documentTemplate.getUserId());
        documentTemplateBo.setTypeId(documentTemplate.getTypeId());
        documentTemplateBo.setInstitutionId(documentTemplate.getInstitutionId());
        return documentTemplateBo;
    }
}
