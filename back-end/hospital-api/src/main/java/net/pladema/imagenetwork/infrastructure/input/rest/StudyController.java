package net.pladema.imagenetwork.infrastructure.input.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import net.pladema.imagenetwork.domain.StudyInfoBo;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.PacUrlDTO;

@RequestMapping("/imagenetwork")
@Tag(name = "Image Network", description = "Image Network")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyController {

	private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;

	@GetMapping(value = "/{studyInstanceUID}")
	public ResponseEntity<PacUrlDTO> getPacGlobalURL(@PathVariable(name = "studyInstanceUID") String studyInstanceUID) {
		log.debug("Input -> studyInstanceUID {}", studyInstanceUID);
		PacUrlDTO url = mapToDto(getPacWhereStudyIsHosted.run(studyInstanceUID));
		log.debug("Output -> {}", url);
		return ResponseEntity.ok().body(url);
	}

	private static PacUrlDTO mapToDto(StudyInfoBo studyInfoBo) {
		return PacUrlDTO.builder()
				.pacGlobalURL(studyInfoBo.getPacGlobalURL())
				.build();
	}

}
