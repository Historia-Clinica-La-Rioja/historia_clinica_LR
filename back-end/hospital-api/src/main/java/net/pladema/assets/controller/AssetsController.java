package net.pladema.assets.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.assets.service.AssetsService;

@Tag(name = "Assets", description = "Assets")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/assets")
public class AssetsController {
    private final AssetsService assetsService;

    @GetMapping(value = "/{fileName:.+}/**")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity getAssetFile(HttpServletRequest request) {
        log.debug("Input parameters -> fileName {} ", request.getRequestURL().toString());

		return StoredFileResponse.sendFile(
				this.assetsService.getFile(getFileName(request))
		);
    }

	private static String getFileName(HttpServletRequest request) {
		String requestURL = request.getRequestURL().toString();
		return requestURL.split("/assets/")[1];
	}
}
