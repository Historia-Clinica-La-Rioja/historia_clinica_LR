package net.pladema.template.application.existsname;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.port.DocumentTemplateStorage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExistsNameDocumentTemplate {
    private final DocumentTemplateStorage documentTemplateStorage;

    public boolean run(Integer userId, String name) {
        log.debug("Input -> userId {} checking document template with name {}", userId, name);

        boolean result = documentTemplateStorage.exists(userId, name);

        log.debug("Output -> {}", result);
        return result;
    }

}
