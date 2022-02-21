package net.pladema.assets.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.assets.service.AssetsService;
import net.pladema.assets.service.domain.AssetsFileBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/assets")
@Tag(name = "Assets", description = "Assets")
public class AssetsController {

    private final Logger logger;

    private static String getFileName(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        return requestURL.split("/assets/")[1];
    }

    private final AssetsService assetsService;

    public AssetsController(AssetsService assetsService) {
        super();
        this.assetsService = assetsService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @GetMapping(value = "/{fileName:.+}/**")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity getAssetFile(HttpServletRequest request) {
        logger.debug("Input parameters -> fileName {} ", request.getRequestURL().toString());

        AssetsFileBo result = this.assetsService.getFile(getFileName(request));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.getContentType()))
                .body(result.getResource());
    }
}
