package net.pladema.staff.controller;

import io.swagger.annotations.Api;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.controller.mocks.HealthcareProfessionalMock;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/institution/{institutionId}/healthcareprofessional")
@Api(value = "Professional", tags = { "Professional" })
public class HealthcareProfessionalController {

	private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalController.class);

	private final HealthcareProfessionalService healthcareProfessionalService;
	
	private final HealthcareProfessionalMapper healthcareProfessionalMapper;

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

	@GetMapping("/search-by-name")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<Collection<ProfessionalDto>> searchByName(@PathVariable(name = "institutionId")  Integer institutionId,
																	@RequestParam(name = "name") String name){
		LOG.debug("Input parameters -> institutionId {}, name {}", institutionId, name);
		Collection<ProfessionalDto> result = HealthcareProfessionalMock.mockListProfessionalDto(name);
		LOG.debug("Output", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{healthcareProfessionalId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<ProfessionalDto> getOne(@PathVariable(name = "institutionId")  Integer institutionId,
												  @PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId){
		LOG.debug("Input parameters -> institutionId {}, healthcareProfessionalId {}", institutionId, healthcareProfessionalId);
		HealthcareProfessionalBo resultService = healthcareProfessionalService.findProfessionalById(healthcareProfessionalId);
		ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(resultService);
		LOG.debug("Output", result);
		return ResponseEntity.ok(result);
	}


}