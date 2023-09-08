package net.pladema.template.application.save;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.application.create.CreateTemplate;
import net.pladema.template.application.port.DocumentTemplateStorage;
import net.pladema.template.domain.DocumentTemplateBo;
import net.pladema.template.domain.enums.EDocumentTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveDocumentTemplate {

    private final DocumentTemplateStorage documentTemplateStorage;
    private final FileService fileService;

    public void run(CreateTemplate createTemplate, DocumentTemplateBo documentTemplateBo) throws JsonProcessingException {

        log.debug("Input -> DocumentTemplateBo {}", documentTemplateBo);

        String json = createTemplate.run(documentTemplateBo);

        FileContentBo templateStream = FileContentBo.fromString(json);

        var path = fileService.buildCompletePath(String.format("/user/%d/templates/%s/%s/",
                documentTemplateBo.getUserId(),
                EDocumentTemplate.map(documentTemplateBo.getTypeId()).getDescription(),
                fileService.createUuid()));
        FileInfo result = fileService.saveStreamInPath(path, documentTemplateBo.getName(), "PLANTILLA", false, templateStream);

        Long fileId = result.getId();
        documentTemplateStorage.save(documentTemplateBo, fileId);

        log.debug("Output -> template {} saved", fileId);
    }


}
