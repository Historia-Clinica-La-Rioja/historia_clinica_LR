package net.pladema.template.application.create;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.domain.ConclusionTemplateBo;
import net.pladema.template.domain.DocumentTemplateBo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateConclusionTemplate implements CreateTemplate {

    private final ObjectMapper objectMapper;

    public String run(DocumentTemplateBo templateBo) throws JsonProcessingException {
        log.trace("Input -> conclusion template {}", templateBo);

        ConclusionTemplateBo conclusionTemplateBo = (ConclusionTemplateBo) templateBo;

        String json = objectMapper.writeValueAsString(conclusionTemplateBo);

        log.trace("Output -> template {} saved", json);

        return json;
    }
}
