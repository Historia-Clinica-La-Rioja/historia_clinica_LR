package net.pladema.template.application.port;

import net.pladema.template.domain.DocumentTemplateBo;

import java.util.List;

public interface DocumentTemplateStorage {

    void save(DocumentTemplateBo documentTemplateBo, Long fileId);

    boolean exists(Integer userId, String name, Short typeId);

    List<DocumentTemplateBo> get(Integer userId, Short typeId);

}
