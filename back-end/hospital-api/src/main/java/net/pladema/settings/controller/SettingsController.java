package net.pladema.settings.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.assets.controller.service.AssetsExternalService;
import net.pladema.settings.service.SettingsService;
import org.apache.http.MethodNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/settings")
@Tag(name = "Settings", description = "Settings")
public class SettingsController {

    private final Logger logger;

    private static String getFileName(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        return requestURL.split("/assets/")[1];
    }

    private final SettingsService settingsService;
    private final AssetsExternalService assetsExternalService;

    public SettingsController(SettingsService settingsService, AssetsExternalService assetsExternalService) {
        this.settingsService = settingsService;
        this.assetsExternalService = assetsExternalService;
        logger = LoggerFactory.getLogger(getClass());
    }

    @PostMapping(value = "/assets/{fileName:.+}/**", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ROOT')")
    public boolean uploadFile(HttpServletRequest request,
                              @RequestPart("file") MultipartFile file) throws MethodNotSupportedException {
        //TODO: en service crear paquete exception con un exception handler en el controller o en otro paquete dentro de la capa de controler. Parserar a apierrordto
        logger.debug("Input parameters -> fileName={}",
                request.getRequestURL());

        return settingsService.uploadFile(this.assetsExternalService.findByName(getFileName(request)), file);
    }

    @DeleteMapping(value = "/assets/{fileName:.+}/**")
    @PreAuthorize("hasAnyAuthority('ROOT')")
    public boolean deleteFile(HttpServletRequest request) throws MethodNotSupportedException {
        logger.debug("Input parameters -> fileName={}",
                request.getRequestURL());

        return settingsService.deleteFile(this.assetsExternalService.findByName(getFileName(request)));
    }
}
