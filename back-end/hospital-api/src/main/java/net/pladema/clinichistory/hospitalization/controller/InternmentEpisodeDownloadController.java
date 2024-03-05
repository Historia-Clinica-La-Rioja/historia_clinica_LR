package net.pladema.clinichistory.hospitalization.controller;


import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.downloadEpisodeDocument.DownloadEpisodeDocument;

@AllArgsConstructor
@Slf4j
@Tag(name = "Internment Episode", description = "Internment Episode")
@Validated
@RestController
@RequestMapping("/institutions/{institutionId}/internments")
public class InternmentEpisodeDownloadController {

	private final FeatureFlagsService featureFlagsService;

	private final DownloadEpisodeDocument downloadEpisodeDocument;

	@GetMapping("/episodedocuments/download/{episodeDocumentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
	public ResponseEntity<Resource> downloadEpisodeDocument(@PathVariable(name = "episodeDocumentId") Integer episodeDocumentId,
															@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> episodeDocumentId {}, institutionId {}", episodeDocumentId, institutionId);
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_DESCARGA_DOCUMENTOS_PDF))
			return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);

		return StoredFileResponse.sendStoredBlob(//InternmentEpisodeDocumentService.download
				downloadEpisodeDocument.run(episodeDocumentId)
		);
	}

}