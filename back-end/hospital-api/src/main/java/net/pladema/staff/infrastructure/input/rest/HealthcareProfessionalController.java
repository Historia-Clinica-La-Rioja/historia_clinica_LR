package net.pladema.staff.infrastructure.input.rest;

import ar.lamansys.sgx.shared.security.UserInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.GetAllProfessionalsAndTechnicians;
import net.pladema.staff.application.fetchprofessionalsbyfilter.FetchProfessionalsByFilter;
import net.pladema.staff.application.getallprofessioinalsandtechniciansbyinstitution.GetAllProfessionalsAndTechniciansByInstitution;
import net.pladema.staff.application.gethealthcareprofessional.GetHealthcareProfessional;
import net.pladema.staff.application.gethealthcareprofessionalbyuserid.GetHealthcareProfessionalByUserId;
import net.pladema.staff.application.getUserIdByHealthcareProfessionalId.GetUserIdByHealthcareProfessionalId;

import net.pladema.staff.application.saveexternaltemporaryprofessional.SaveExternalTemporaryProfessional;
import net.pladema.staff.application.saveprofessional.SaveHealthcareProfessional;
import net.pladema.staff.controller.dto.ExternalTemporaryHealthcareProfessionalDto;
import net.pladema.staff.controller.dto.HealthcareProfessionalCompleteDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.domain.HealthcareProfessionalSearchBo;
import net.pladema.staff.service.HealthcareProfessionalService;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/healthcareprofessional")
//@Api(value = "Healthcare professionals", tags = { "Healthcare professionals" })
public class HealthcareProfessionalController {

	public static final String OUTPUT = "Output -> {}";

	private final HealthcareProfessionalService healthcareProfessionalService;

	private final HealthcareProfessionalMapper healthcareProfessionalMapper;

	private final GetHealthcareProfessional getHealthcareProfessional;

	private final SaveHealthcareProfessional saveHealthcareProfessional;
	private final GetHealthcareProfessionalByUserId getHealthcareProfessionalByUserId;

	private final GetUserIdByHealthcareProfessionalId getUserIdByHealthcareProfessionalId;

	private final GetAllProfessionalsAndTechnicians getAllProfessionalsAndTechnicians;

	private final SaveExternalTemporaryProfessional saveExternalTemporaryProfessional;
	private final FetchProfessionalsByFilter fetchProfessionalsByFilter;
	private final ObjectMapper jackson;
	private final GetAllProfessionalsAndTechniciansByInstitution getAllProfessionalsAndTechniciansByInstitution;

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<ProfessionalDto> getAll() {
		List<HealthcareProfessionalBo> professionals = healthcareProfessionalService.getAllProfessional();
		log.debug("Get all professional => {}", professionals);
		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(professionals);
		log.debug(OUTPUT, result);
		return result;
	}

	@GetMapping("/get-all-professionals-and-technicians")
	public List<ProfessionalDto> getAllProfessionalsAndTechnicians() {
		log.debug("No input parameters");
		List<HealthcareProfessionalBo> professionals = getAllProfessionalsAndTechnicians.run();
		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(professionals);
		log.debug(OUTPUT, result);
		return result;
	}

	@GetMapping("institution/{institutionId}/get-all-professionals-and-technicians")

	public List<ProfessionalDto> getAllProfessionalsAndTechniciansByInstitution(
			@PathVariable(name = "institutionId") Integer institutionId
	) {
		log.debug("Input parameters -> {}",institutionId);
		List<HealthcareProfessionalBo> professionals = getAllProfessionalsAndTechniciansByInstitution.run(institutionId);
		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(professionals);
		log.debug(OUTPUT, result);
		return result;
	}

	@GetMapping("/institution/{institutionId}/person/{personId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR')")
	public ResponseEntity<ProfessionalDto> get(@PathVariable(name = "institutionId") Integer institutionId,
											   @PathVariable(name = "personId") Integer personId) {
		log.debug("Input parameters -> {}", personId);
		ProfessionalDto result = healthcareProfessionalMapper.fromProfessionalBo(getHealthcareProfessional.execute(personId));
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "/{healthcareProfessionalId}")
	public ResponseEntity<Integer> getUserIdByHealthcareProfessionalId(@PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId) {
		log.debug("Input parameters -> {}", healthcareProfessionalId);
		Integer result = getUserIdByHealthcareProfessionalId.execute(healthcareProfessionalId);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/by-user-logged")
	public ResponseEntity<Integer> getProfessionalIdByUser() {
		log.debug("No input parameters");
		Integer result = getHealthcareProfessionalByUserId.execute(UserInfo.getCurrentAuditor());
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping(value = "/institution/{institutionId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR')")
	public ResponseEntity<Integer> save(@PathVariable(name = "institutionId") Integer institutionId,
										  @RequestBody HealthcareProfessionalCompleteDto professionalDto) {
		log.debug("Input parameters -> {}", professionalDto);
		Integer result = saveHealthcareProfessional.run(healthcareProfessionalMapper.fromHealthcareProfessionalCompleteDto(professionalDto));
		log.debug(OUTPUT,result);
		return ResponseEntity.ok().body(result);
	}


	@PostMapping(value = "/institution/{institutionId}/external-temporary")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR')")
	public ResponseEntity<Integer> createTemporal(@PathVariable(name = "institutionId") Integer institutionId,
										@RequestBody ExternalTemporaryHealthcareProfessionalDto temporaryProfessionalDto) {
		log.debug("Input parameters -> temporaryProfessionalDto {}", temporaryProfessionalDto);
		Integer result = saveExternalTemporaryProfessional.run(healthcareProfessionalMapper.fromExternalTemporaryHealthcareProfessionalDto(temporaryProfessionalDto));
		log.debug(OUTPUT,result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "/department/{departmentId}")
	@PreAuthorize("hasAnyAuthority('GESTOR_CENTRO_LLAMADO')")
	public List<ProfessionalDto> getAllByDepartment(@PathVariable("departmentId") Short departmentId) {
		log.debug("Input parameter -> departmentId {}", departmentId);
		List<HealthcareProfessionalBo> professionals = healthcareProfessionalService.getAllProfessionalsByDepartment(departmentId);
		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(professionals);
		log.debug(OUTPUT, result);
		return result;
	}

	@GetMapping(value = "/by-filter")
	@PreAuthorize("hasAnyAuthority('GESTOR_CENTRO_LLAMADO')")
	public List<ProfessionalDto> getAllByFilter(@RequestParam String searchFilter) {
		log.debug("Input parameter getAllByFilter -> filter {}", searchFilter);
		List<ProfessionalDto> result = healthcareProfessionalMapper.fromProfessionalBoList(
				fetchProfessionalsByFilter.execute(parseFilter(searchFilter))
		);
		log.debug(OUTPUT, result);
		return result;
	}

	private HealthcareProfessionalSearchBo parseFilter(String filter) {
		HealthcareProfessionalSearchBo searchFilter = null;
		try {
			searchFilter = jackson.readValue(filter, HealthcareProfessionalSearchBo.class);
		} catch (IOException e) {
			log.error(String.format("Error mapping filter: %s", filter), e);
		}
		return searchFilter;
	}
}