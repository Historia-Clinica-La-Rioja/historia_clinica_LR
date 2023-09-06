package net.pladema.template.application.port;

import net.pladema.template.domain.DocumentTemplateBo;

public interface DocumentTemplateStorage {

    void save(DocumentTemplateBo documentTemplateBo, Long fileId);

    boolean exists(Integer userId, String name);
}
