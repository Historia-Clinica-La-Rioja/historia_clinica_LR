package net.pladema.staff.controller;

import io.swagger.annotations.Api;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/healthcareprofessional")
@Api(value = "Healthcare professionals", tags = { "Healthcare professionals" })
public class HealthcareProfessionalController {

	private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalController.class);
	public static final String OUTPUT = "Output -> {}";

	private final HealthcareProfessionalService healthcareProfessionalService;

	private final HealthcareProfessionalMapper healthcareProfessionalMapper;

	public HealthcareProfessionalController(HealthcareProfessionalService healthcareProfessionalService,
											HealthcareProfessionalMapper healthcareProfessionalMapper) {
		this.healthcareProfessionalService = healthcareProfessionalService;
		this.healthcareProfessionalMapper = healthcareProfessionalMapper;
	}


	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<ProfessionalDto> getAll() {
		List<HealthcareProfessionalBo> professionals = healthcareProfessionalService.getAllProfessional();
		LOG.debug("Get all professional => {}", professionals);
		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(professionals);
		LOG.debug(OUTPUT, result);
		return result;
	}
}