package net.pladema.template.application.delete;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.port.DocumentTemplateStorage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteTemplate {
    private final DocumentTemplateStorage documentTemplateStorage;

    public boolean run(Integer userId, Short typeId, Long id) {
        log.debug("Input -> userId {} deleting document template (typeId {}) with id {}", userId, typeId, id);

        documentTemplateStorage.delete(id);

        log.debug("Output -> {}", true);
        return true;
    }
}
