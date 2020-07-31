package net.pladema.staff.controller;

import java.util.List;

import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
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

import io.swagger.annotations.Api;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;
@RestController
@RequestMapping("/institution/{institutionId}/healthcareprofessional")
@Api(value = "Professional", tags = { "Professional" })
public class HealthcareProfessionalController {

	private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalController.class);

	private HealthcareProfessionalService healthcareProfessionalService;
	
	private HealthcareProfessionalMapper healthcareProfessionalMapper;

	public HealthcareProfessionalController(HealthcareProfessionalService healthcareProfessionalService,
											HealthcareProfessionalMapper healthcareProfessionalMapper) {
		this.healthcareProfessionalService = healthcareProfessionalService;
		this.healthcareProfessionalMapper = healthcareProfessionalMapper;
	}


	@GetMapping("/doctors")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<List<HealthcareProfessionalDto>> getAllDoctors(@PathVariable(name = "institutionId")  Integer institutionId){
		List<HealthcarePersonBo> doctors = healthcareProfessionalService.getAllDoctorsByInstitution(institutionId);
		LOG.debug("Get all Doctors => {}", doctors);
		List<HealthcareProfessionalDto> result = healthcareProfessionalMapper.fromHealthcarePersonList(doctors);
		LOG.debug("Output", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<List<ProfessionalDto>> getAll(
			@PathVariable(name = "institutionId")  Integer institutionId){
		LOG.debug("Input parameters -> institutionId {}", institutionId);
		List<HealthcareProfessionalBo> healthcareProfessionals = healthcareProfessionalService.getAll(institutionId);
		LOG.debug("Get all Healthcare professional => {}", healthcareProfessionals);
		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(healthcareProfessionals);
		LOG.debug("Output", result);
		return ResponseEntity.ok(result);
	}

}