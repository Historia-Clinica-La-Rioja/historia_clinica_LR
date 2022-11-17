package net.pladema.staff.infrastructure.input.rest;

import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.staff.application.gethealthcareprofessional.GetHealthcareProfessional;
import net.pladema.staff.application.gethealthcareprofessionalbyuserid.GetHealthcareProfessionalByUserId;
import net.pladema.staff.application.saveprofessional.SaveHealthcareProfessional;
import net.pladema.staff.controller.dto.HealthcareProfessionalCompleteDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/healthcareprofessional")
//@Api(value = "Healthcare professionals", tags = { "Healthcare professionals" })
public class HealthcareProfessionalController {

	private static final Logger LOG = LoggerFactory.getLogger(HealthcareProfessionalController.class);
	public static final String OUTPUT = "Output -> {}";

	private final HealthcareProfessionalService healthcareProfessionalService;

	private final HealthcareProfessionalMapper healthcareProfessionalMapper;

	private final GetHealthcareProfessional getHealthcareProfessional;

	private final SaveHealthcareProfessional saveHealthcareProfessional;

	private final GetHealthcareProfessionalByUserId getHealthcareProfessionalByUserId;

	public HealthcareProfessionalController(HealthcareProfessionalService healthcareProfessionalService,
											HealthcareProfessionalMapper healthcareProfessionalMapper,
											GetHealthcareProfessional getHealthcareProfessional,
											SaveHealthcareProfessional saveHealthcareProfessional,
											GetHealthcareProfessionalByUserId getHealthcareProfessionalByUserId) {
		this.healthcareProfessionalService = healthcareProfessionalService;
		this.healthcareProfessionalMapper = healthcareProfessionalMapper;
		this.getHealthcareProfessional = getHealthcareProfessional;
		this.saveHealthcareProfessional = saveHealthcareProfessional;
		this.getHealthcareProfessionalByUserId = getHealthcareProfessionalByUserId;
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

	@GetMapping("/institution/{institutionId}/person/{personId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<ProfessionalDto> get(@PathVariable(name = "institutionId") Integer institutionId,
											   @PathVariable(name = "personId") Integer personId) {
		LOG.debug("Input parameters -> {}", personId);
		ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(getHealthcareProfessional.execute(personId));
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/by-user-logged")
	public ResponseEntity<Integer> getProfessionalIdByUser() {
		LOG.debug("No input parameters");
		Integer result = getHealthcareProfessionalByUserId.execute(UserInfo.getCurrentAuditor());
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping(value = "/institution/{institutionId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<Integer> save(@PathVariable(name = "institutionId") Integer institutionId,
										  @RequestBody HealthcareProfessionalCompleteDto professionalDto) {
		LOG.debug("Input parameters -> {}", professionalDto);
		Integer result = saveHealthcareProfessional.run(healthcareProfessionalMapper.fromHealthcareProfessionalCompleteDto(professionalDto));
		LOG.debug(OUTPUT,result);
		return ResponseEntity.ok().body(result);
	}

}