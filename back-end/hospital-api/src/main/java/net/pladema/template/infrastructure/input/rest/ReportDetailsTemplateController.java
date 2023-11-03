package net.pladema.template.infrastructure.input.rest;

import ar.lamansys.sgx.shared.security.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.create.CreateTextTemplate;
import net.pladema.template.application.get.GetTemplate;
import net.pladema.template.application.save.SaveDocumentTemplate;
import net.pladema.template.domain.TextTemplateBo;
import net.pladema.template.domain.enums.EDocumentTemplate;
import net.pladema.template.infrastructure.input.rest.dto.TextTemplateDto;
import net.pladema.template.infrastructure.input.rest.mapper.DocumentTemplateMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@Tag(name = "Document Template Report Details", description = "Document Template Report Details")
@RequestMapping("/institutions/{institutionId}/documents/report-details/templates")
@PreAuthorize("hasPermission(#institutionId, 'INFORMADOR')")
@Slf4j
@RequiredArgsConstructor
public class ReportDetailsTemplateController {

    private final DocumentTemplateMapper documentTemplateMapper;
    private final SaveDocumentTemplate saveDocumentTemplate;
    private final CreateTextTemplate createTextTemplate;
    private final GetTemplate getTemplate;

    @PostMapping
    public ResponseEntity<Boolean> save(@PathVariable(name = "institutionId") Integer institutionId,
                                        @Valid @RequestBody TextTemplateDto textTemplateDto) throws JsonProcessingException {
        log.trace("Input -> institutionId {}, textTemplateDto {}", institutionId, textTemplateDto);
        TextTemplateBo textTemplateBo = documentTemplateMapper.toTextTemplateBo(textTemplateDto);
        textTemplateBo.setUserId(UserInfo.getCurrentAuditor());
        textTemplateBo.setTypeId(EDocumentTemplate.REPORT_DETAILS_RDI.getId());
        textTemplateBo.setInstitutionId(institutionId);
        saveDocumentTemplate.run(createTextTemplate, textTemplateBo);

        log.trace("Output -> {}", true);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<TextTemplateDto> getOne(@PathVariable(name = "institutionId") Integer institutionId,
                                               @PathVariable(name = "templateId") Long templateId) {
        log.trace("Input -> institutionId {}, templateId {}", institutionId, templateId);

        TextTemplateBo textTemplateBo = getTemplate.run(templateId, TextTemplateBo.class);
        var result = documentTemplateMapper.toTextTemplateDto(textTemplateBo);

        log.trace("Output -> {}", result);
        return ResponseEntity.ok(result);
    }

}
