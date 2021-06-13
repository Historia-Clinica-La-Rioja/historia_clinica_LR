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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/assets")
@Api(value = "Assets", tags = { "Assets" })
public class AssetsController {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRequestController.class);

    private static String getFileName(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        return requestURL.split("/assets/")[1];
    }

    private final AssetsService assetsService;

    public AssetsController(AssetsService assetsService) {
        super();
        this.assetsService = assetsService;
    }

    @GetMapping(value = "/{fileName:.+}/**")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity getAssetFile(HttpServletRequest request) {
        LOG.debug("Input parameters -> fileName {} ", request.getRequestURL().toString());

        AssetsFileBo result = this.assetsService.getFile(getFileName(request));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.getContentType()))
                .body(result.getResource());
    }
}
