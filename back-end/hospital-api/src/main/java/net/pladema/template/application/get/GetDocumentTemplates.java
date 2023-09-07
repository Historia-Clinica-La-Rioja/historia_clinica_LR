package net.pladema.template.application.get;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.port.DocumentTemplateStorage;
import net.pladema.template.domain.DocumentTemplateBo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetDocumentTemplates {

    private final DocumentTemplateStorage documentTemplateStorage;

    public List<DocumentTemplateBo> run(Integer userId, Short typeId) {
        log.debug("Input -> userId {}, typeId {}", userId, typeId);

        List<DocumentTemplateBo> result = documentTemplateStorage.get(userId, typeId);

        log.debug("Output -> templates returned {}", result);
        return result;
    }
}
