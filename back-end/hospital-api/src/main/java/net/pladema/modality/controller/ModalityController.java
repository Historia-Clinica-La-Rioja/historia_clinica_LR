package net.pladema.modality.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.modality.controller.dto.ModalityDto;
import net.pladema.modality.service.ModalityBOMapper;
import net.pladema.modality.service.ModalityService;
import net.pladema.modality.service.domain.ModalityBO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Modality", description = "Modality")
@RequestMapping("/institutions/{institutionId}")
@Slf4j
@RequiredArgsConstructor
public class ModalityController {

	private final ModalityService modalityService;

	private final ModalityBOMapper modalityBOMapper;
	
	@GetMapping(value = "/modality")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA, TECNICO, INFORMADOR, INDEXADOR')")
	public ResponseEntity<List<ModalityDto>> getAllModality(
			@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("{}", "All modalities");
		List<ModalityBO> modalitiesBO = modalityService.getAllModality();
		List<ModalityDto> modalitiesDto = modalityBOMapper.toListModalityDto(modalitiesBO);
		return ResponseEntity.ok().body(modalitiesDto);
	}

	@GetMapping(value = "/modalities-by-studies-completed")
	@PreAuthorize("hasPermission(#institutionId, 'INFORMADOR')")
	public ResponseEntity<List<ModalityDto>> getModalitiesByStudiesCompleted(
			@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<ModalityBO> modalitiesBO = modalityService.getModalitiesByStudiesCompleted(institutionId);
		List<ModalityDto> modalitiesDto = modalityBOMapper.toListModalityDto(modalitiesBO);
		return ResponseEntity.ok().body(modalitiesDto);
	}
}
