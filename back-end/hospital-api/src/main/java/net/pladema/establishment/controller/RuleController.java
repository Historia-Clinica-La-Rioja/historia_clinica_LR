package net.pladema.establishment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.rules.determineregulatedreference.DetermineRegulatedReference;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Rules", description = "Rules")
@RequestMapping("/institutions/{institutionId}/rules")
@RestController
public class RuleController {

	private final DetermineRegulatedReference determineRegulatedReference;

	@GetMapping("/validate-regulation")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Boolean> determineRegulatedReference(@PathVariable(name = "institutionId") Integer institutionId,
															   @RequestParam(value = "clinicalSpecialtyIds", required = false) List<Integer> clinicalSpecialtyIds,
															   @RequestParam(value = "practiceId", required = false) Integer practiceId) {
		log.debug("Input parameters -> institutionId {}, clinicalSpecialtyIds {}, practiceId {} ", institutionId, clinicalSpecialtyIds, practiceId);
		boolean result = determineRegulatedReference.run(institutionId, clinicalSpecialtyIds, practiceId);
		log.debug("Output result -> {}", result);
		return ResponseEntity.ok(result);
	}

}
