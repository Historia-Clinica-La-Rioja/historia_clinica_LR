package net.pladema.template.infrastructure.input.rest;

import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.get.GetDocumentTemplates;
import net.pladema.template.infrastructure.input.rest.constraints.ValidTypeDocumentTemplate;
import net.pladema.template.infrastructure.input.rest.dto.TemplateNamesDto;
import net.pladema.template.infrastructure.input.rest.mapper.DocumentTemplateMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Document Template", description = "Document Template")
@RequestMapping("/institutions/{institutionId}/documents/templates/{typeId}")
@Validated
@Slf4j
@RequiredArgsConstructor
public class DocumentTemplateController {

    private final DocumentTemplateMapper documentTemplateMapper;
    private final GetDocumentTemplates getDocumentTemplates;

    @GetMapping("/user")
    @ValidTypeDocumentTemplate
    public ResponseEntity<List<TemplateNamesDto>> get(@PathVariable(name = "institutionId") Integer institutionId,
                                                      @PathVariable(name = "typeId") Short typeId) {
        Integer userId = UserInfo.getCurrentAuditor();
        log.trace("Input -> institutionId {}, userId {}, typeId {}", institutionId, userId, typeId);

        List<TemplateNamesDto> result = getDocumentTemplates.run(userId, typeId)
                .stream()
                .map(documentTemplateMapper::toTemplateNamesDto)
                .collect(Collectors.toList());

        log.trace("Output -> result {}", result);
        return ResponseEntity.ok(result);

    }
}
