package net.pladema.establishment.infrastructure.input.rest;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.practices.GetPractices;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/institution/{institutionId}")
@Tag(name = "Practices", description = "Practices")
@Slf4j
@RequiredArgsConstructor
@RestController
public class PracticesController {

	private final GetPractices getPractices;

	@GetMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<SharedSnomedDto>> getPractices(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {} ", institutionId);
		List<SharedSnomedDto> result = getPractices.run(institutionId);
		log.debug("Get practices -> ", result);
		return ResponseEntity.ok().body(result);
	}

}
