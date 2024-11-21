package net.pladema.establishment.infrastructure.input.rest;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.institutionpractices.GetPracticesFromInstitutionsByCareLineId;
import net.pladema.establishment.application.practices.GetPracticesByActiveDiaries;
import net.pladema.establishment.application.institutionpractices.GetPracticesByInstitution;
import net.pladema.establishment.application.institutionpractices.GetPracticesFromInstitutions;

import net.pladema.establishment.application.practices.GetPracticesByDepartmentId;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/practices")
@Tag(name = "Practices", description = "Practices")
@Slf4j
@RequiredArgsConstructor
@RestController
public class PracticesController {

	private final GetPracticesByInstitution getPracticesByInstitution;

	private final GetPracticesByActiveDiaries getPracticesByActiveDiaries;

	private final GetPracticesFromInstitutions getPracticesFromInstitutions;

	private final GetPracticesFromInstitutionsByCareLineId getPracticesFromInstitutionsByCareLineId;

	private final GetPracticesByDepartmentId getPracticesByDepartmentId;

	@GetMapping("/institution/{institutionId}/by-institution")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_AGENDA') || hasAnyAuthority('GESTOR_CENTRO_LLAMADO')")
	public ResponseEntity<List<SharedSnomedDto>> getPractices(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {} ", institutionId);
		List<SharedSnomedDto> result = getPracticesByInstitution.run(institutionId);
		log.debug("Get practices by institution -> ", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/institution/{institutionId}/by-active-diaries")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, GESTOR_DE_ACCESO_INSTITUCIONAL')")
	public ResponseEntity<List<SharedSnomedDto>> getByActiveDiaries(
			@PathVariable(name = "institutionId") Integer institutionId) {
		List<SharedSnomedDto> activeDiariesPractices = getPracticesByActiveDiaries.run(institutionId);
		log.debug("Get all practices by active diaries and Institution {} ", institutionId);
		return ResponseEntity.ok(activeDiariesPractices);
	}

	@GetMapping("/institution/{institutionId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRATIVO')")
	public ResponseEntity<List<SharedSnomedDto>> getPracticesFromInstitutions(@PathVariable(name = "institutionId") Integer institutionId) {
		List<SharedSnomedDto> result = getPracticesFromInstitutions.run();
		log.debug("Get practices from all institutions -> ", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping()
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL', " +
			"'ESPECIALISTA_MEDICO', 'PROFESIONAL_DE_SALUD', 'ESPECIALISTA_EN_ODONTOLOGIA', 'ADMINISTRATIVO', 'ABORDAJE_VIOLENCIAS', 'GESTOR_DE_ACCESO_INSTITUCIONAL')")
	public ResponseEntity<List<SharedSnomedDto>> getAll(@RequestParam(name = "careLineId", required = false) Integer careLineId) {
		log.debug("Input parameteres -> careLineId {}", careLineId);
		List<SharedSnomedDto> result = getPracticesFromInstitutionsByCareLineId.run(careLineId);
		log.debug("Get practices from domain -> ", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/department/{departmentId}")
	@PreAuthorize("hasAnyAuthority('GESTOR_CENTRO_LLAMADO')")
	public ResponseEntity<List<SharedSnomedDto>> getAllByDepartmentId(@PathVariable(name = "departmentId") Short departmentId) {
		log.debug("Input parameteres -> departmentId {}", departmentId);
		List<SharedSnomedDto> result = getPracticesByDepartmentId.run(departmentId);
		log.debug("Practices from department -> {}", result);
		return ResponseEntity.ok().body(result);
	}

}
