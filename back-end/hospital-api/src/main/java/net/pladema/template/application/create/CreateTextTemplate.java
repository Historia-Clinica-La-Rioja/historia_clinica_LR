package net.pladema.template.application.create;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.domain.DocumentTemplateBo;
import net.pladema.template.domain.TextTemplateBo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateTextTemplate implements CreateTemplate {

    private final ObjectMapper objectMapper;

    public String run(DocumentTemplateBo templateBo) throws JsonProcessingException {
        log.trace("Input -> text template {}", templateBo);

        TextTemplateBo textTemplateBo = (TextTemplateBo) templateBo;

        String json = objectMapper.writeValueAsString(textTemplateBo);

        log.trace("Output -> template {}", json);

        return json;
    }
}
