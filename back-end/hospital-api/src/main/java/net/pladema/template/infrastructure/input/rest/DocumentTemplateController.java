package net.pladema.template.infrastructure.input.rest;

import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.delete.DeleteTemplate;
import net.pladema.template.application.get.GetListTemplates;
import net.pladema.template.infrastructure.input.rest.constraints.ValidTypeDocumentTemplate;
import net.pladema.template.infrastructure.input.rest.dto.TemplateNamesDto;
import net.pladema.template.infrastructure.input.rest.mapper.DocumentTemplateMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final GetListTemplates getListTemplates;
    private final DeleteTemplate deleteTemplate;

    @GetMapping("/user")
    @ValidTypeDocumentTemplate
    public ResponseEntity<List<TemplateNamesDto>> get(@PathVariable(name = "institutionId") Integer institutionId,
                                                      @PathVariable(name = "typeId") Short typeId) {
        Integer userId = UserInfo.getCurrentAuditor();
        log.trace("Input -> institutionId {}, userId {}, typeId {}", institutionId, userId, typeId);

        List<TemplateNamesDto> result = getListTemplates.run(userId, typeId)
                .stream()
                .map(documentTemplateMapper::toTemplateNamesDto)
                .collect(Collectors.toList());

        log.trace("Output -> result {}", result);
        return ResponseEntity.ok(result);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "institutionId") Integer institutionId,
                                          @PathVariable(name = "typeId") Short typeId,
                                          @PathVariable(name = "id") Long id) {
        Integer userId = UserInfo.getCurrentAuditor();
        log.trace("Input -> institutionId {}, userId {}, typeId {}, documentTemplateId {}", institutionId, userId, typeId, id);
        deleteTemplate.run(userId, typeId, id);
        log.trace("Output -> result {}", true);
        return ResponseEntity.ok(true);
    }

}
