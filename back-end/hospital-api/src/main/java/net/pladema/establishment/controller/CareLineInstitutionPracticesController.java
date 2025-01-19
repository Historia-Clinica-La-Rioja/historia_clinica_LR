package net.pladema.establishment.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.carelineinstitutionpractices.GetCareLineInstitutionPractices;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Care line Institution Practice", description = "Care line Institution Practice")
@RequestMapping("/institution/{institutionId}/careline-institution-practice")
@RestController
public class CareLineInstitutionPracticesController {

	private final GetCareLineInstitutionPractices getCareLineInstitutionPractices;

	@GetMapping(value = "/careLine/{careLineId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRATIVO, GESTOR_DE_ACCESO_INSTITUCIONAL')")
	public ResponseEntity<List<SnomedDto>> getPracticesByCareLine(@PathVariable(name = "institutionId") Integer institutionId,
																  @PathVariable(name = "careLineId") Integer careLineId) {
		log.debug("Input parameters -> careLineId {}", careLineId);
		var result = getCareLineInstitutionPractices.run(careLineId)
				.stream()
				.map(SnomedDto::from)
				.collect(Collectors.toList());
		return ResponseEntity.ok(result);
	}
}
