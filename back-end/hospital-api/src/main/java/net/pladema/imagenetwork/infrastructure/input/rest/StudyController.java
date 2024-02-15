package net.pladema.imagenetwork.infrastructure.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.pladema.imagenetwork.application.getfileinfo.getfileuuid.GetFileUuid;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import net.pladema.imagenetwork.domain.PacsListBo;
import net.pladema.imagenetwork.domain.StudyFileInfoBo;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.PacsListDto;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.StudyFileInfoDto;
import net.pladema.imagenetwork.infrastructure.input.rest.mapper.ImageNetworkMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.generatetokenstudypermissions.GenerateStudyTokenJWT;
import net.pladema.imagenetwork.application.generatetokenstudypermissions.GenerateStudyTokenUUID;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.TokenDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/institutions/{institutionId}/imagenetwork/{studyInstanceUID}")
@Tag(name = "Image Network", description = "Image Network")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyController {
	private final ImageNetworkMapper imageNetworkMapper;
	private final GenerateStudyTokenJWT generateStudyTokenJWT;
	private final GenerateStudyTokenUUID generateStudyTokenUUID;
	private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;
	private final GetFileUuid getFileUuid;

	@GetMapping(value = "/pacs")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, INFORMADOR, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<PacsListDto> getPACS(@PathVariable Integer institutionId, @PathVariable String studyInstanceUID) {
		log.trace("Input -> institutionId '{}' studyInstanceUID '{}'", institutionId, studyInstanceUID);
		PacsListBo pacsListBo = getPacWhereStudyIsHosted.run(studyInstanceUID, false);
		PacsListDto result = imageNetworkMapper.toPacsUrlDto(pacsListBo);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/permission/generate/uuid")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, INFORMADOR, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<TokenDto> generatePermissionsUUID(@PathVariable String studyInstanceUID, @PathVariable Integer institutionId) {
		log.trace("Input -> studyInstanceUID '{}' institutionId '{}'", studyInstanceUID, institutionId);
		String result = generateStudyTokenUUID.run(studyInstanceUID);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok()
				.body(new TokenDto(result));
	}

	@GetMapping("/permission/generate/jwt")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, INFORMADOR, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<TokenDto> generatePermissionsJWT(@PathVariable String studyInstanceUID, @PathVariable Integer institutionId) {
		log.trace("Input -> studyInstanceUID '{}' institutionId '{}'", studyInstanceUID, institutionId);
		String result = generateStudyTokenJWT.run(studyInstanceUID);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok()
				.body(new TokenDto(result));
	}

	@PostMapping(value = "/file-info")
	@PreAuthorize("hasPermission(#institutionId, 'INFORMADOR')")
	public ResponseEntity<StudyFileInfoDto> getStudyFileInfo(@PathVariable Integer institutionId,
															 @PathVariable String studyInstanceUID,
															 @RequestBody PacsListDto pacs) throws JsonProcessingException {
		log.trace("Input -> institutionId '{}' studyInstanceUID '{}' pacs '{}'", institutionId, studyInstanceUID, pacs);
		StudyFileInfoBo studyFileInfoBo = getFileUuid.run(studyInstanceUID, pacs.getUrls());
		var result = imageNetworkMapper.toStudyFileInfoDto(studyFileInfoBo);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}
}
