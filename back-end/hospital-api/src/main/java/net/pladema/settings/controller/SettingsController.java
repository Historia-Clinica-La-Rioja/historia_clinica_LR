package net.pladema.settings.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.requests.servicerequests.controller.ServiceRequestController;
import net.pladema.settings.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/settings")
@Api(value = "Settings", tags = { "Settings" })
public class SettingsController {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRequestController.class);
    private static final String OUTPUT = "create result -> {}";

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PostMapping(value = "/assets/{fileName:.+}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    public boolean uploadFile(@PathVariable(name = "fileName") String fileName,
                                @RequestPart("file") MultipartFile file) {
        LOG.debug("Input parameters ->  {} fileName {}",
                fileName);
        return settingsService.execute(fileName, file);
    }

    @DeleteMapping(value = "/assets/{fileName:.+}")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    public boolean deleteFile(@PathVariable(name = "fileName") String fileName) {
        LOG.debug("Input parameters ->  {} fileName {}",
                fileName);
        return settingsService.deleteFile(fileName);
    }
}
