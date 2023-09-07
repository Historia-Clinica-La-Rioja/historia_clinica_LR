package net.pladema.template.infrastructure.input.rest;

import ar.lamansys.sgx.shared.security.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.createconclusiontemplate.CreateConclusionTemplate;
import net.pladema.template.application.save.SaveDocumentTemplate;
import net.pladema.template.domain.ConclusionTemplateBo;
import net.pladema.template.infrastructure.input.rest.constraints.ValidDocumentTemplate;
import net.pladema.template.infrastructure.input.rest.dto.ConclusionTemplateDto;
import net.pladema.template.infrastructure.input.rest.mapper.DocumentTemplateMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Tag(name = "Document Template Conclusions", description = "Document Template Conclusions")
@RequestMapping("/institutions/{institutionId}/documents/templates/conclusions")
@PreAuthorize("hasPermission(#institutionId, 'INFORMADOR')")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ConclusionTemplateController {

    private final DocumentTemplateMapper documentTemplateMapper;
    private final SaveDocumentTemplate saveDocumentTemplate;
    private final CreateConclusionTemplate createConclusionTemplate;

    @PostMapping("/save")
    @ValidDocumentTemplate
    public ResponseEntity<Boolean> save(@PathVariable(name = "institutionId") Integer institutionId,
                                        @RequestBody ConclusionTemplateDto conclusionTemplateDto) throws JsonProcessingException {
        log.trace("Input -> institutionId {}, conclusionTemplateDto {}", institutionId, conclusionTemplateDto);
        ConclusionTemplateBo conclusionTemplateBo = documentTemplateMapper.toConclusionTemplateBo(conclusionTemplateDto);
        conclusionTemplateBo.setUserId(UserInfo.getCurrentAuditor());
        conclusionTemplateBo.setInstitutionId(institutionId);
        String template = createConclusionTemplate.run(conclusionTemplateBo);
        saveDocumentTemplate.run(conclusionTemplateBo, Optional.ofNullable(template));

        log.trace("Output -> {}", true);
        return ResponseEntity.ok(true);

    }
}
