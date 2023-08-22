package ar.lamansys.sgh.publicapi.imagenetwork.infrastructure.input.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.imagenetwork.infrastructure.input.rest.dto.TokenDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedStudyPermissionPort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "PublicApi ImageNetwork", description = "Check study token")
@RequestMapping("/public-api/imagenetwork/{studyInstanceUID}/permission/check")
public class CheckStudyTokenController {

	private final SharedStudyPermissionPort sharedStudyPermissionPort;

	@GetMapping
	public ResponseEntity<TokenDto> checkPermissions(@PathVariable String studyInstanceUID, @RequestParam("token") String tokenStudy) {
		log.trace("Input -> studyInstanceUID '{}' tokenStudy '{}'", studyInstanceUID, tokenStudy);
		String result = sharedStudyPermissionPort.checkTokenStudyPermissions(studyInstanceUID, tokenStudy);
		log.trace("Output -> {}", result);
		return ResponseEntity.ok()
				.body(new TokenDto(result));
	}
}
