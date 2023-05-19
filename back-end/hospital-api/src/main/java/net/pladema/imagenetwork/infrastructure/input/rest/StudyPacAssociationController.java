package net.pladema.imagenetwork.infrastructure.input.rest;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import net.pladema.imagenetwork.domain.PacsListBo;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.PacsUrlDto;

@RequestMapping("/institutions/{institutionId}/imagenetwork/pacs")
@Tag(name = "Image Network Pacs", description = "Image Network Pacs")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyPacAssociationController {

	private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;

	@GetMapping(value = "/{studyInstanceUID}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, INFORMADOR')")
	public ResponseEntity<PacsUrlDto> getPacGlobalURL(@PathVariable Integer institutionId, @PathVariable String studyInstanceUID) throws MalformedURLException {
		log.debug("Input -> institutionId '{}' studyInstanceUID '{}'", institutionId, studyInstanceUID);
		PacsUrlDto url = mapToDto(getPacWhereStudyIsHosted.run(studyInstanceUID));
		log.debug("Output -> {}", url);
		return ResponseEntity.ok().body(url);
	}

	private static PacsUrlDto mapToDto(PacsListBo pacsListBo) {
		return PacsUrlDto.builder().pacs(pacsListBo
						.getPacs().stream()
						.map(URI::toString)
						.collect(Collectors.toList()))
				.build();
	}
}
