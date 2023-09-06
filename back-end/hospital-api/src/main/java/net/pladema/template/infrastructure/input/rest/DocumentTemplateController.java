package net.pladema.template.infrastructure.input.rest;

import ar.lamansys.sgx.shared.security.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.save.SaveDocumentTemplate;
import net.pladema.template.domain.DocumentTemplateBo;
import net.pladema.template.infrastructure.input.rest.constraints.ValidDocumentTemplate;
import net.pladema.template.infrastructure.input.rest.dto.DocumentTemplateDto;
import net.pladema.template.infrastructure.input.rest.mapper.DocumentTemplateMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Document Template", description = "Document Template")
@RequestMapping("/institutions/{institutionId}/documents/templates")
@Validated
@Slf4j
@RequiredArgsConstructor
public class DocumentTemplateController {
    private final SaveDocumentTemplate saveDocumentTemplate;
    private final DocumentTemplateMapper documentTemplateMapper;

    @PostMapping("/save")
    @ValidDocumentTemplate
    public ResponseEntity<Boolean> save(@PathVariable(name = "institutionId") Integer institutionId,
                                        @RequestBody DocumentTemplateDto templateDto) throws JsonProcessingException {
        log.trace("Input -> institutionId {}, documentTemplateDto {}", institutionId, templateDto);
        DocumentTemplateBo documentTemplateBo = documentTemplateMapper.toDocumentTemplateBo(templateDto);
        documentTemplateBo.setUserId(UserInfo.getCurrentAuditor());
        documentTemplateBo.setInstitutionId(institutionId);

        saveDocumentTemplate.run(documentTemplateBo);

        log.trace("Output -> {}", true);
        return ResponseEntity.ok(true);

    }
}
