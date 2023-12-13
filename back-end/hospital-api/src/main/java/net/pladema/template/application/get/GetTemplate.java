package net.pladema.template.application.get;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hsi.extensions.utils.JsonResourceUtils;
import net.pladema.template.application.port.DocumentTemplateStorage;
import net.pladema.template.domain.exceptions.DocumentTemplateException;
import net.pladema.template.domain.exceptions.EDocumentTemplateException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class GetTemplate {

    private final DocumentTemplateStorage documentTemplateStorage;
    private final FileService fileService;

    public <T> T run(Long templateId, Class<T> templateClass) {
        log.debug("Input -> templateId {}", templateId);

        Long fileId = documentTemplateStorage.getFileId(templateId)
                        .orElseThrow(() -> new DocumentTemplateException(EDocumentTemplateException.NOT_FOUND, "document.template.error.id.not-found"));

        FileContentBo file = fileService.loadFile(fileId);
        return JsonResourceUtils.readJson(file.getStream(), templateClass, null);
    }


}
