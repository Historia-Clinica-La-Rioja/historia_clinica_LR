package net.pladema.template.infrastructure.input.rest;

import ar.lamansys.sgx.shared.security.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.create.CreateConclusionTemplate;
import net.pladema.template.application.save.SaveDocumentTemplate;
import net.pladema.template.domain.ConclusionTemplateBo;
import net.pladema.template.domain.enums.EDocumentTemplate;
import net.pladema.template.infrastructure.input.rest.dto.ConclusionTemplateDto;
import net.pladema.template.infrastructure.input.rest.mapper.DocumentTemplateMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@Tag(name = "Document Conclusions Template", description = "Document Conclusions Template")
@RequestMapping("/institutions/{institutionId}/documents/conclusions/templates")
@PreAuthorize("hasPermission(#institutionId, 'INFORMADOR')")
@Slf4j
@RequiredArgsConstructor
public class ConclusionTemplateController {

    private final DocumentTemplateMapper documentTemplateMapper;
    private final SaveDocumentTemplate saveDocumentTemplate;
    private final CreateConclusionTemplate createConclusionTemplate;

    @PostMapping
    public ResponseEntity<Boolean> save(@PathVariable(name = "institutionId") Integer institutionId,
                                        @Valid @RequestBody ConclusionTemplateDto conclusionTemplateDto) throws JsonProcessingException {
        log.trace("Input -> institutionId {}, conclusionTemplateDto {}", institutionId, conclusionTemplateDto);
        ConclusionTemplateBo conclusionTemplateBo = documentTemplateMapper.toConclusionTemplateBo(conclusionTemplateDto);
        conclusionTemplateBo.setUserId(UserInfo.getCurrentAuditor());
        conclusionTemplateBo.setTypeId(EDocumentTemplate.INFORMER_CONCLUSIONS.getId());
        conclusionTemplateBo.setInstitutionId(institutionId);
        saveDocumentTemplate.run(createConclusionTemplate, conclusionTemplateBo);

        log.trace("Output -> {}", true);
        return ResponseEntity.ok(true);

    }
}
