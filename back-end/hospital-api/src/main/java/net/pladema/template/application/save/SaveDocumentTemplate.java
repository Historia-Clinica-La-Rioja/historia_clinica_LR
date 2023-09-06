package net.pladema.template.application.save;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.port.DocumentTemplateStorage;
import net.pladema.template.domain.DocumentTemplateBo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveDocumentTemplate {

    private final DocumentTemplateStorage documentTemplateStorage;
    private final FileService fileService;
    private final ObjectMapper objectMapper;

    public void run(DocumentTemplateBo documentTemplateBo) throws JsonProcessingException {
        log.debug("Input -> template {}", documentTemplateBo);

        String json = objectMapper.writeValueAsString(documentTemplateBo);
        FileContentBo templateStream = FileContentBo.fromString(json);

        var path = fileService.buildCompletePath(String.format("/user/%d/templates/%s/", documentTemplateBo.getUserId(), fileService.createUuid()));
        FileInfo result = fileService.saveStreamInPath(path, documentTemplateBo.getName(), "PLANTILLA", false, templateStream);

        Long fileId = result.getId();
        documentTemplateStorage.save(documentTemplateBo, fileId);

        log.debug("Output -> template {} saved", fileId);
    }


}
