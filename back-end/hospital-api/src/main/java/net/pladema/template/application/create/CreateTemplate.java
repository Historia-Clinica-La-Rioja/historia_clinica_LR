package net.pladema.template.application.create;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.pladema.template.domain.DocumentTemplateBo;

public interface CreateTemplate {

    String run(DocumentTemplateBo templateBo) throws JsonProcessingException;
}
