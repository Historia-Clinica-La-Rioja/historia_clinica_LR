package net.pladema.imagenetwork.infrastructure.input.rest;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

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
import net.pladema.imagenetwork.domain.PacsListBo;
import net.pladema.imagenetwork.domain.StudyPacBo;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.PacsUrlDTO;
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
	public ResponseEntity<PacsUrlDTO> getPacGlobalURL(@PathVariable(name = "studyInstanceUID") String studyInstanceUID) throws MalformedURLException {
		log.debug("Input -> studyInstanceUID {}", studyInstanceUID);
		PacsUrlDTO url = mapToDto(getPacWhereStudyIsHosted.run(studyInstanceUID));
		log.debug("Output -> {}", url);
		return ResponseEntity.ok().body(url);
	}

	@PostMapping(value = "/")
	public ResponseEntity<String> saveStudyPacGlobalAssociation(@RequestBody StudyPacAssociationDTO studyPacAssociationDTO) throws URISyntaxException {
		log.debug("Input -> studyPacAssociationDTO {}", studyPacAssociationDTO);
		String study = savePacWhereStudyIsHosted.run(mapToBo(studyPacAssociationDTO));
		log.debug("Output -> {}", study);
		return ResponseEntity.ok().body(study);
	}

	private static PacsUrlDTO mapToDto(PacsListBo pacsListBo) {
		return PacsUrlDTO.builder().pacs(pacsListBo
						.getPacs().stream()
						.map(URI::toString)
						.collect(Collectors.toList()))
				.build();
	}

	private static StudyPacBo mapToBo(StudyPacAssociationDTO dto) throws URISyntaxException {
		return new StudyPacBo(dto.getStudyInstanceUID(), dto.getPacGlobalURL());
	}

}
