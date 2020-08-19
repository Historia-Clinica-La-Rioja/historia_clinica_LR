package net.pladema.staff.controller;

import io.swagger.annotations.Api;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institution/{institutionId}/healthcareprofessional")
@Api(value = "Professional", tags = { "Professional" })
public class HealthcareProfessionalController {

	private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalController.class);
	public static final String OUTPUT = "Output -> {}";

	private final HealthcareProfessionalService healthcareProfessionalService;
	
	private final HealthcareProfessionalMapper healthcareProfessionalMapper;

	private final LoggedUserExternalService loggedUserExternalService;

	public HealthcareProfessionalController(HealthcareProfessionalService healthcareProfessionalService,
											HealthcareProfessionalMapper healthcareProfessionalMapper,
											LoggedUserExternalService loggedUserExternalService) {
		this.healthcareProfessionalService = healthcareProfessionalService;
		this.healthcareProfessionalMapper = healthcareProfessionalMapper;
		this.loggedUserExternalService = loggedUserExternalService;
	}


	@GetMapping("/doctors")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<HealthcareProfessionalDto>> getAllDoctors(@PathVariable(name = "institutionId")  Integer institutionId){
		boolean isAdministrativeRole = loggedUserExternalService.hasAnyRoleInstitution(institutionId, List.of(ERole.ADMINISTRATIVO, ERole.ADMINISTRADOR_AGENDA));
		List<HealthcarePersonBo> doctors = healthcareProfessionalService.getAllDoctorsByInstitution(institutionId);
		if (!isAdministrativeRole) {
			Integer healthcareProfessionalId = healthcareProfessionalService.getProfessionalId(UserInfo.getCurrentAuditor());
			Predicate<HealthcarePersonBo> filterByProfessionalId = (hp) -> hp.getId().equals(healthcareProfessionalId);
			doctors = doctors.stream().filter(filterByProfessionalId).collect(Collectors.toList());
		}
		LOG.debug("Get all Doctors => {}", doctors);
		List<HealthcareProfessionalDto> result = healthcareProfessionalMapper.fromHealthcarePersonList(doctors);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<ProfessionalDto>> getAll(
			@PathVariable(name = "institutionId")  Integer institutionId){
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		List<HealthcareProfessionalBo> healthcareProfessionals = healthcareProfessionalService.getAll(institutionId);
		LOG.debug("Get all Healthcare professional => {}", healthcareProfessionals);
		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(healthcareProfessionals);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{healthcareProfessionalId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<ProfessionalDto> getOne(@PathVariable(name = "institutionId")  Integer institutionId,
												  @PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId){
		LOG.debug("Input parameters -> institutionId {}, healthcareProfessionalId {}", institutionId, healthcareProfessionalId);
		HealthcareProfessionalBo resultService = healthcareProfessionalService.findProfessionalById(healthcareProfessionalId);
		ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(resultService);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}


}