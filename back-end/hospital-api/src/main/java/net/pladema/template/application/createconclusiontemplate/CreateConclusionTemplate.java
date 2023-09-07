package net.pladema.template.application.createconclusiontemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.domain.ConclusionTemplateBo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateConclusionTemplate {

    private final ObjectMapper objectMapper;

    public String run(ConclusionTemplateBo conclusionTemplateBo) throws JsonProcessingException {
        log.trace("Input -> conclusion template {}", conclusionTemplateBo);

        String json = objectMapper.writeValueAsString(conclusionTemplateBo);

        log.trace("Output -> template {} saved", json);

        return json;
    }
}
