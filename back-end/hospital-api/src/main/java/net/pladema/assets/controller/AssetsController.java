package net.pladema.assets.controller;

import io.swagger.annotations.Api;
import net.pladema.assets.service.AssetsService;
import net.pladema.assets.service.domain.AssetsFileBo;
import net.pladema.clinichistory.requests.servicerequests.controller.ServiceRequestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assets")
@Api(value = "Assets", tags = { "Assets" })
public class AssetsController {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRequestController.class);

    private final AssetsService assetsService;

    public AssetsController(AssetsService assetsService) {
        super();
        this.assetsService = assetsService;
    }

    @GetMapping(value = "/{fileName:.+}")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity getAssetFile(@PathVariable(name = "fileName") String fileName) {
        LOG.debug("Input parameters -> fileName {} ", fileName);
        AssetsFileBo result = this.assetsService.getFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.getContentType()))
                .body(result.getResource());
    }
}
