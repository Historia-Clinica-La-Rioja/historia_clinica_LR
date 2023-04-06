package net.pladema.imagenetwork.infrastructure.input.rest;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import net.pladema.imagenetwork.application.savepacsherestudyishosted.SavePacWhereStudyIsHosted;
import net.pladema.imagenetwork.domain.StudyInfoBo;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.PacUrlDTO;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.StudyPacAssociationDTO;

@RequestMapping("/imagenetwork")
@Tag(name = "Image Network", description = "Image Network")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyController {

	private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;
	private final SavePacWhereStudyIsHosted savePacWhereStudyIsHosted;

	@GetMapping(value = "/{studyInstanceUID}")
	public ResponseEntity<PacUrlDTO> getPacGlobalURL(@PathVariable(name = "studyInstanceUID") String studyInstanceUID) throws MalformedURLException {
		log.debug("Input -> studyInstanceUID {}", studyInstanceUID);
		PacUrlDTO url = mapToDto(getPacWhereStudyIsHosted.run(studyInstanceUID));
		log.debug("Output -> {}", url);
		return ResponseEntity.ok().body(url);
	}

	@PostMapping(value = "/")
	public ResponseEntity<String> saveStudyPacGlobalAssociation(@RequestBody StudyPacAssociationDTO studyPacAssociationDTO) throws MalformedURLException, URISyntaxException {
		log.debug("Input -> studyPacAssociationDTO {}", studyPacAssociationDTO);
		String study = savePacWhereStudyIsHosted.run(mapToBo(studyPacAssociationDTO));
		log.debug("Output -> {}", study);
		return ResponseEntity.ok().body(study);
	}

	private static PacUrlDTO mapToDto(StudyInfoBo studyInfoBo) {
		return PacUrlDTO.builder()
				.pacGlobalURL(studyInfoBo.getPacGlobalURL().toString())
				.build();
	}

	private static StudyInfoBo mapToBo(StudyPacAssociationDTO dto) throws MalformedURLException, URISyntaxException {
		return new StudyInfoBo(dto.getStudyInstanceUID(), dto.getPacGlobalURL());
	}

}
