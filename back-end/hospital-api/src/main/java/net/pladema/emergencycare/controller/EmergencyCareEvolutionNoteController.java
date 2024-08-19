package net.pladema.emergencycare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.pladema.emergencycare.application.createemergencycareevolutionnote.CreateEmergencyCareEvolutionNote;
import net.pladema.emergencycare.application.getevolutionnotebydocumentid.GetEvolutionNoteByDocumentId;
import net.pladema.emergencycare.application.updateemergencycareevolutionnote.UpdateEmergencyCareEvolutionNote;
import net.pladema.emergencycare.controller.dto.EmergencyCareEvolutionNoteDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareEvolutionNoteMapper;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteBo;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;

@Tag(name = "Emergency care evolution notes", description = "Emergency care evolution notes")
@Validated
@AllArgsConstructor
@RequestMapping("/institution/{institutionId}/emergency-care/episodes/{episodeId}/evolution-note")
@RestController
public class EmergencyCareEvolutionNoteController {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEvolutionNoteController.class);

	private final EmergencyCareEvolutionNoteMapper emergencyCareEvolutionNoteMapper;

	private final GetEvolutionNoteByDocumentId getEvolutionNoteByDocumentId;

	private final CreateEmergencyCareEvolutionNote createEmergencyCareEvolutionNote;

	private final UpdateEmergencyCareEvolutionNote updateEmergencyCareEvolutionNote;


	@PostMapping
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Boolean> createEmergencyCareEvolutionNote(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId,
			@RequestBody EmergencyCareEvolutionNoteDto evolutionNote
	){
		LOG.debug("Parameters -> institutionId {}, episodeId {}, evolutionNote {}", institutionId, episodeId, evolutionNote);
		EmergencyCareEvolutionNoteDocumentBo emergencyCareEvolutionNoteDocumentBo = emergencyCareEvolutionNoteMapper.fromEmergencyCareEvolutionNoteDto(evolutionNote);
		EmergencyCareEvolutionNoteBo result = createEmergencyCareEvolutionNote.run(institutionId, episodeId, emergencyCareEvolutionNoteDocumentBo);
		LOG.debug("Output -> result {}", result);
		return ResponseEntity.ok().body(true);
	}

	@PostMapping("/edit-by-document/{documentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Boolean> updateEmergencyCareEvolutionNote(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId,
			@PathVariable(name = "documentId") Long oldDocumentId,
			@RequestBody EmergencyCareEvolutionNoteDto evolutionNote
	){
		LOG.debug("Parameters -> institutionId {}, episodeId {}, documentId {}, evolutionNote {}", institutionId, episodeId, oldDocumentId, evolutionNote);
		EmergencyCareEvolutionNoteDocumentBo emergencyCareEvolutionNoteDocumentBo = emergencyCareEvolutionNoteMapper.fromEmergencyCareEvolutionNoteDto(evolutionNote);
		EmergencyCareEvolutionNoteBo result = updateEmergencyCareEvolutionNote.run(institutionId, episodeId, oldDocumentId, emergencyCareEvolutionNoteDocumentBo);
		LOG.debug("result -> {}", result);
		return ResponseEntity.ok().body(true);
	}

	@GetMapping("/by-document/{documentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<EmergencyCareEvolutionNoteDto> getByDocumentId(@PathVariable(name = "institutionId") Integer institutionId,
																		 @PathVariable(name = "episodeId") Integer episodeId,
																		 @PathVariable(name = "documentId") Long documentId)
	{
		LOG.debug("Input parameters -> institutionId {}, episodeId{}, documentId{}", institutionId, episodeId, documentId);
		EmergencyCareEvolutionNoteDocumentBo evolutionNoteBo = getEvolutionNoteByDocumentId.run(documentId);
		EmergencyCareEvolutionNoteDto result = emergencyCareEvolutionNoteMapper.toEmergencyCareEvolutionNoteDto(evolutionNoteBo);
		LOG.debug("Output -> result {}", result);
		return ResponseEntity.ok(result);
	}


}
