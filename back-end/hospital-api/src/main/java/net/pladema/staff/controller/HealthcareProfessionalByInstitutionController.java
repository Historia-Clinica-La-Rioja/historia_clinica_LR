package net.pladema.staff.controller;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.pladema.medicalconsultation.diary.service.DiaryAssociatedProfessionalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

@RestController
@RequestMapping("/institution/{institutionId}/healthcareprofessional")
@Tag(name = "Healthcare professionals by institution", description = "Healthcare professionals by institution")
public class HealthcareProfessionalByInstitutionController {

	private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalByInstitutionController.class);
	public static final String OUTPUT = "Output -> {}";

	private final HealthcareProfessionalService healthcareProfessionalService;
	
	private final HealthcareProfessionalMapper healthcareProfessionalMapper;

	private final LoggedUserExternalService loggedUserExternalService;

	private final DiaryAssociatedProfessionalService diaryAssociatedProfessionalService;

	public HealthcareProfessionalByInstitutionController(HealthcareProfessionalService healthcareProfessionalService,
														 HealthcareProfessionalMapper healthcareProfessionalMapper,
														 LoggedUserExternalService loggedUserExternalService,
														 DiaryAssociatedProfessionalService diaryAssociatedProfessionalService) {
		this.healthcareProfessionalService = healthcareProfessionalService;
		this.healthcareProfessionalMapper = healthcareProfessionalMapper;
		this.loggedUserExternalService = loggedUserExternalService;
		this.diaryAssociatedProfessionalService = diaryAssociatedProfessionalService;
	}


	@GetMapping("/doctors")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_AGENDA, ENFERMERO')")
	public ResponseEntity<List<HealthcareProfessionalDto>> getAllDoctors(@PathVariable(name = "institutionId")  Integer institutionId){
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		boolean isAdministrativeRole = loggedUserExternalService.hasAnyRoleInstitution(institutionId, ERole.ADMINISTRATIVO, ERole.ADMINISTRADOR_AGENDA);
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
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<List<ProfessionalDto>> getAllByInstitution(
			@PathVariable(name = "institutionId")  Integer institutionId){
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		boolean isAdministrativeRole = loggedUserExternalService.hasAnyRoleInstitution(institutionId,
				ERole.ADMINISTRATIVO, ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.PERSONAL_DE_ESTADISTICA);
		List<HealthcareProfessionalBo> healthcareProfessionals = healthcareProfessionalService.getAllByInstitution(institutionId);
		if (!isAdministrativeRole) {
			Integer healthcareProfessionalId = healthcareProfessionalService.getProfessionalId(UserInfo.getCurrentAuditor());
			Predicate<HealthcareProfessionalBo> filterByProfessionalId = (hp) -> hp.getId().equals(healthcareProfessionalId);
			healthcareProfessionals = healthcareProfessionals.stream().filter(filterByProfessionalId).collect(Collectors.toList());
		}
		LOG.debug("Get all Healthcare professional => {}", healthcareProfessionals);
		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(healthcareProfessionals);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/associated-healthcare-professionals")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<List<ProfessionalDto>> getAllByDiaryAndInstitution(@PathVariable(name = "institutionId") Integer institutionId) {
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		boolean isAdministrativeRole = loggedUserExternalService.hasAnyRoleInstitution(institutionId,
				ERole.ADMINISTRATIVO, ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.PERSONAL_DE_ESTADISTICA);
		List<HealthcareProfessionalBo> healthcareProfessionals = healthcareProfessionalService.getAllByInstitution(institutionId);
		Integer healthcareProfessionalId = healthcareProfessionalService.getProfessionalId(UserInfo.getCurrentAuditor());
		if (!isAdministrativeRole) {
			List<Integer> associatedHealthcareProfessionals = diaryAssociatedProfessionalService.getAllAssociatedWithProfessionalsByHealthcareProfessionalId(institutionId, healthcareProfessionalId);
			healthcareProfessionals = healthcareProfessionals.stream().filter(healthcareProfessional ->
					healthcareProfessional.getId().equals(healthcareProfessionalId) || associatedHealthcareProfessionals.contains(healthcareProfessional.getId())
			).collect(Collectors.toList());
		}
		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(healthcareProfessionals);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{healthcareProfessionalId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<ProfessionalDto> getOne(@PathVariable(name = "institutionId")  Integer institutionId,
												  @PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId){
		LOG.debug("Input parameters -> institutionId {}, healthcareProfessionalId {}", institutionId, healthcareProfessionalId);
		HealthcareProfessionalBo resultService = healthcareProfessionalService.findActiveProfessionalById(healthcareProfessionalId);
		ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(resultService);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/associated-healthcare-professionals-with-active-diaries")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<List<ProfessionalDto>> getAllByDiaryActiveAndInstitution(@PathVariable(name = "institutionId") Integer institutionId) {
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		List<HealthcareProfessionalBo> healthcareProfessionals = healthcareProfessionalService.getAllByInstitution(institutionId);
		Integer healthcareProfessionalId = healthcareProfessionalService.getProfessionalId(UserInfo.getCurrentAuditor());

		List<Integer> associatedHealthcareProfessionals = diaryAssociatedProfessionalService.getAllAssociatedWithProfessionalsByHealthcareProfessionalIdAndActiveDiaries(institutionId, healthcareProfessionalId);
		healthcareProfessionals = healthcareProfessionals.stream().filter(healthcareProfessional ->
				healthcareProfessional.getId().equals(healthcareProfessionalId) || associatedHealthcareProfessionals.contains(healthcareProfessional.getId())
		).collect(Collectors.toList());

		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(healthcareProfessionals);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}


}