package net.pladema.imagenetwork.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.checktokenstudypermissions.CheckTokenStudyPermissions;
import net.pladema.imagenetwork.application.generatetokenstudypermissions.GenerateStudyTokenJWT;
import net.pladema.imagenetwork.application.generatetokenstudypermissions.GenerateStudyTokenUUID;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.TokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/institutions/{institutionId}/imagenetwork/{studyInstanceUID}/permission")
@Tag(name = "Image Network Study Permission", description = "Image Network Study Permission")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyPermissionController {

	private final CheckTokenStudyPermissions checkTokenStudyPermissions;
	private final GenerateStudyTokenJWT generateStudyTokenJWT;
	private final GenerateStudyTokenUUID generateStudyTokenUUID;

	@GetMapping("/check")
	public ResponseEntity<TokenDTO> checkPermissions(@PathVariable String studyInstanceUID, @RequestParam("token") String tokenStudy) {
		log.trace("Input -> studyInstanceUID '{}' tokenStudy '{}'", studyInstanceUID, tokenStudy);
		String result = checkTokenStudyPermissions.run(studyInstanceUID, tokenStudy);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok()
				.body(new TokenDTO(result));
	}

	@GetMapping("/generate/uuid")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, INFORMADOR')")
	public ResponseEntity<TokenDTO> generatePermissionsUUID(@PathVariable String studyInstanceUID, @PathVariable Integer institutionId) {
		log.trace("Input -> studyInstanceUID '{}' institutionId '{}'", studyInstanceUID, institutionId);
		String result = generateStudyTokenUUID.run(studyInstanceUID);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok()
				.body(new TokenDTO(result));
	}

	@GetMapping("/generate/jwt")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, INFORMADOR')")
	public ResponseEntity<TokenDTO> generatePermissionsJWT(@PathVariable String studyInstanceUID, @PathVariable Integer institutionId) {
		log.trace("Input -> studyInstanceUID '{}' institutionId '{}'", studyInstanceUID, institutionId);
		String result = generateStudyTokenJWT.run(studyInstanceUID);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok()
				.body(new TokenDTO(result));
	}
}
