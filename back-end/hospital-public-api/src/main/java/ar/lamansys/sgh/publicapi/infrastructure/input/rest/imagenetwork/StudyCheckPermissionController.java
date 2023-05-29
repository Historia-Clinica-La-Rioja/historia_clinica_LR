package ar.lamansys.sgh.publicapi.infrastructure.input.rest.imagenetwork;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.imagenetwork.TokenDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedStudyPermissionPort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/public-api/imagenetwork/{studyInstanceUID}/permission")
@Tag(name = "Public Api", description = "Image Network Study Permission")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyCheckPermissionController {

	private final SharedStudyPermissionPort sharedStudyPermissionPort;

	@GetMapping("/check")
	public ResponseEntity<TokenDto> checkPermissions(@PathVariable String studyInstanceUID, @RequestParam("token") String tokenStudy) {
		log.trace("Input -> studyInstanceUID '{}' tokenStudy '{}'", studyInstanceUID, tokenStudy);
		String result = sharedStudyPermissionPort.checkTokenStudyPermissions(studyInstanceUID, tokenStudy);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok()
				.body(new TokenDto(result));
	}
}
