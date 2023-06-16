package net.pladema.documentpublicaccess.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import net.pladema.documentpublicaccess.service.PrescriptionFileService;

import java.io.IOException;

@RestController
@RequestMapping("/external-document-access")
public class DocumentPublicAccessController {
	private final Logger logger;
	private final PrescriptionFileService prescriptionFileService;

	public DocumentPublicAccessController(PrescriptionFileService prescriptionFileService) {
		super();
		this.prescriptionFileService = prescriptionFileService;
		this.logger = LoggerFactory.getLogger(getClass());
	}

	@GetMapping(value = "/download-prescription/{accessId}")
	@CrossOrigin
	public ResponseEntity getPrescription(@PathVariable("accessId") String accessId) throws IOException {
		logger.debug("Input parameters -> accessId for prescription file {} ", accessId);

		var result = this.prescriptionFileService.getFile(accessId);

		if(result.isEmpty()) {
			return ResponseEntity.badRequest().body("El documento solicitado no existe");
		}

		StoredFileBo pdfFile = result.get();

		return StoredFileResponse.sendFile(pdfFile);

	}
}

