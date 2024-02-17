package net.pladema.template.application.port;

import net.pladema.template.domain.DocumentTemplateBo;

import java.util.List;
import java.util.Optional;

public interface DocumentTemplateStorage {

    void save(DocumentTemplateBo documentTemplateBo, Long fileId);

    boolean exists(Integer userId, String name, Short typeId);

    List<DocumentTemplateBo> get(Integer userId, Short typeId);

    Optional<Long> getFileId(Long id);

    void delete(Long id);

}
