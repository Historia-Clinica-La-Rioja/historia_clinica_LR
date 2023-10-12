package net.pladema.imagenetwork.infrastructure.input.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.generatetokenstudypermissions.GenerateStudyTokenJWT;
import net.pladema.imagenetwork.application.generatetokenstudypermissions.GenerateStudyTokenUUID;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.TokenDto;

@RequestMapping("/institutions/{institutionId}/imagenetwork/{studyInstanceUID}/permission")
@Tag(name = "Image Network Study Permission", description = "Image Network Study Permission")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyPermissionController {
	private final GenerateStudyTokenJWT generateStudyTokenJWT;
	private final GenerateStudyTokenUUID generateStudyTokenUUID;

	@GetMapping("/generate/uuid")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, INFORMADOR, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<TokenDto> generatePermissionsUUID(@PathVariable String studyInstanceUID, @PathVariable Integer institutionId) {
		log.trace("Input -> studyInstanceUID '{}' institutionId '{}'", studyInstanceUID, institutionId);
		String result = generateStudyTokenUUID.run(studyInstanceUID);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok()
				.body(new TokenDto(result));
	}

	@GetMapping("/generate/jwt")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, INFORMADOR, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<TokenDto> generatePermissionsJWT(@PathVariable String studyInstanceUID, @PathVariable Integer institutionId) {
		log.trace("Input -> studyInstanceUID '{}' institutionId '{}'", studyInstanceUID, institutionId);
		String result = generateStudyTokenJWT.run(studyInstanceUID);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok()
				.body(new TokenDto(result));
	}
}
