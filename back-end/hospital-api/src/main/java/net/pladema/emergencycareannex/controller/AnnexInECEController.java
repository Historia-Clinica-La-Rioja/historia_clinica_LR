package net.pladema.emergencycareannex.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.emergencycareannex.domain.service.AnnexInECEService;

@RestController
@RequestMapping
@Tag(name = "Annex in emergency care episode", description = "Anexo en módulo de guardia")
public class AnnexInECEController {
	private final Logger logger = LoggerFactory.getLogger(AnnexInECEController.class);

	private final AnnexInECEService service;

	@Autowired
	public AnnexInECEController(AnnexInECEService service) {
		this.service = service;
	}

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@GetMapping("/institution/{institutionId}/emergency-care/episodes/{episodeId}/annex-pdf")
	public ResponseEntity<InputStreamResource> downloadEpisodePdf(@PathVariable Integer institutionId, @PathVariable Integer episodeId) throws PDFDocumentException {
		logger.debug("input parameters -> institutionId: {}, episodeId: {}", institutionId, episodeId);

		FileContentBo fileContentBo;
		try {
			fileContentBo = service.generateEpisodePdf(episodeId);
			logger.info("Successfully generated PDF for episodeId: {}", episodeId);
		} catch (Exception e) {
			logger.error("Error generating PDF for episodeId: {}", episodeId, e);
			throw e;
		}

		String outputFileName = "Anexo II - Guardia - ID " + episodeId + ".pdf";

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			fileContentBo.getStream().transferTo(outputStream);
			logger.info("Successfully transferred PDF content to output stream for episodeId: {}", episodeId);
		} catch (IOException e) {
			logger.error("Error transferring stream to output stream", e);
			throw new RuntimeException("Error while transferring the PDF content", e);
		}

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		InputStreamResource resource = new InputStreamResource(inputStream);

		logger.info("Returning PDF response for episodeId: {}", episodeId);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + outputFileName)
				.contentType(MediaType.APPLICATION_PDF)
				.contentLength(outputStream.size())
				.body(resource);
	}
}
