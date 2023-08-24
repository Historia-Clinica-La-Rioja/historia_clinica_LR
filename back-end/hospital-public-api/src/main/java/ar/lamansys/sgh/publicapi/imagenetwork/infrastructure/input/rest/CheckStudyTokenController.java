package ar.lamansys.sgh.publicapi.imagenetwork.infrastructure.input.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.imagenetwork.application.check.CheckStudyPermission;
import ar.lamansys.sgh.publicapi.imagenetwork.application.check.exceptions.BadStudyTokenException;
import ar.lamansys.sgh.publicapi.imagenetwork.infrastructure.input.rest.dto.TokenDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@Tag(name = "PublicApi ImageNetwork", description = "CheckStudyToken")
@RequestMapping("/public-api/imagenetwork/{studyInstanceUID}/permission/check")
public class CheckStudyTokenController {

	private final CheckStudyPermission checkStudyPermission;

	@GetMapping
	public @ResponseBody TokenDto
	checkPermissions(
			@PathVariable String studyInstanceUID,
			@RequestParam("token") String tokenStudy
	) throws BadStudyTokenException {
		log.trace("Input -> studyInstanceUID '{}' tokenStudy '{}'", studyInstanceUID, tokenStudy);
		String result = checkStudyPermission.run(studyInstanceUID, tokenStudy);
		log.trace("Output -> {}", result);
		return new TokenDto(result);
	}
}
