package net.pladema.staff.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.service.domain.HealthcarePerson;

@RestController
@RequestMapping("/institution/{institutionId}/healthcareprofessional")
@Api(value = "Professional", tags = { "Professional" })
public class HealthcareProfessionalController {

	private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalController.class);

	private HealthcareProfessionalRepository healthcareProfessionalRepository;
	
	private HealthcareProfessionalMapper healthcareProfessionalMapper;

	
	
	public HealthcareProfessionalController(HealthcareProfessionalRepository healthcareProfessionalRepository,
			HealthcareProfessionalMapper healthcareProfessionalMapper) {
		this.healthcareProfessionalRepository = healthcareProfessionalRepository;
		this.healthcareProfessionalMapper = healthcareProfessionalMapper;
	}


	@GetMapping("/doctors")
	public ResponseEntity<List<HealthcareProfessionalDto>> getAllDoctors(@PathVariable(name = "institutionId")  Integer institutionId){
		List<HealthcarePerson> doctors = healthcareProfessionalRepository.getAllDoctors(institutionId);
		LOG.debug("Get all Doctors => {}", doctors);
		return ResponseEntity.ok(healthcareProfessionalMapper.fromHealthcarePersonList(doctors));
	}

}