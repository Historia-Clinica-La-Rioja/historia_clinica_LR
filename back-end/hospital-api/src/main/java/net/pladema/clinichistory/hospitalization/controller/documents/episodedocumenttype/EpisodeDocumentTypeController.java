package net.pladema.clinichistory.hospitalization.controller.documents.episodedocumenttype;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.application.fetchEpisodeDocumentType.FetchEpisodeDocumentType;

import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentTypeDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/episodedocumenttypes")
@Tag(name = "Episode document type", description = "Episode document type")
@AllArgsConstructor
@Slf4j
public class EpisodeDocumentTypeController {

	private final FetchEpisodeDocumentType fetchEpisodeDocumentType;

	@GetMapping("")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
	public ResponseEntity<List<EpisodeDocumentTypeDto>> getEpisodeDocumentTypeList(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<EpisodeDocumentTypeDto> result = fetchEpisodeDocumentType.run()
				.stream()
				.map(bo -> new EpisodeDocumentTypeDto(bo))
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}
}
