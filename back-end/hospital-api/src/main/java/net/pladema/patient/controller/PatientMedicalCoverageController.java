package net.pladema.patient.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.mapper.PatientMedicalCoverageMapper;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/patientMedicalCoverage")
@Validated
@Tag(name = "Patient Medical Coverage", description = "Patient Medical Coverage")
public class PatientMedicalCoverageController {

	private static final Logger LOG = LoggerFactory.getLogger(PatientMedicalCoverageController.class);

	public static final String PATIENTINPUT = "Input data -> patientId {}";

	public static final String OUTPUT = "Output -> {}";

	public static final String PATIENTMC_NOT_FOUND = "patientmc.not.found";

	private final PatientMedicalCoverageService patientMedicalCoverageService;

	private final PatientMedicalCoverageMapper patientMedicalCoverageMapper;

	public PatientMedicalCoverageController(PatientMedicalCoverageService patientMedicalCoverageService, PatientMedicalCoverageMapper patientMedicalCoverageMapper) {
		this.patientMedicalCoverageService = patientMedicalCoverageService;
		this.patientMedicalCoverageMapper = patientMedicalCoverageMapper;
	}

	@GetMapping(value = "/{patientId}/coverages")
	public ResponseEntity<List<PatientMedicalCoverageDto>> getActivePatientMedicalCoverages(
			@PathVariable(name = "patientId") Integer patientId) {
		LOG.debug(PATIENTINPUT, patientId);
		List<PatientMedicalCoverageBo> serviceResult = patientMedicalCoverageService.getActiveCoverages(patientId);
		List<PatientMedicalCoverageDto> result = patientMedicalCoverageMapper.toListPatientMedicalCoverageDto(serviceResult);
		LOG.debug("result -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "/{patientMedicalCoverageId}")
	public ResponseEntity<PatientMedicalCoverageDto> getPatientMedicalCoverage(
			@PathVariable(value = "patientMedicalCoverageId", required = true) Integer patientMedicalCoverageId) {
		LOG.debug("Input data -> patientMedicalCoverageId {}", patientMedicalCoverageId);
		PatientMedicalCoverageBo serviceResult = patientMedicalCoverageService.getCoverage(patientMedicalCoverageId)
				.orElseThrow(() -> new NotFoundException("bad-pmc-id", PATIENTMC_NOT_FOUND));
		PatientMedicalCoverageDto result = patientMedicalCoverageMapper.toPatientMedicalCoverageDto(serviceResult);
		LOG.debug("result -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "/{patientId}/healthInsurances")
	public ResponseEntity<List<PatientMedicalCoverageDto>> getActivePatientHealthInsurances(
			@PathVariable(name = "patientId", required = true) Integer patientId) {
		LOG.debug(PATIENTINPUT, patientId);
		List<PatientMedicalCoverageBo> serviceResult = patientMedicalCoverageService.getActiveHealthInsurances(patientId);
		List<PatientMedicalCoverageDto> result = patientMedicalCoverageMapper.toListPatientMedicalCoverageDto(serviceResult);
		LOG.debug("result -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "/{patientId}/privateHealthInsurances")
	public ResponseEntity<List<PatientMedicalCoverageDto>> getActivePrivateMedicalCoverages(
			@PathVariable(name = "patientId") Integer patientId) {
		LOG.debug(PATIENTINPUT, patientId);
		List<PatientMedicalCoverageBo> serviceResult = patientMedicalCoverageService.getActivePrivateHealthInsurances(patientId);
		List<PatientMedicalCoverageDto> result = patientMedicalCoverageMapper.toListPatientMedicalCoverageDto(serviceResult);
		LOG.debug("Ids results -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/{patientId}/coverages")
	public ResponseEntity<List<Integer>> addPatientMedicalCoverages(
			@PathVariable(name = "patientId") Integer patientId,
			@RequestBody List<PatientMedicalCoverageDto> coverages) throws URISyntaxException {
		LOG.debug("Input data -> coverages {} ", coverages);
		List<Integer> result = patientMedicalCoverageService.saveCoverages(patientMedicalCoverageMapper.toListPatientMedicalCoverageBo(coverages), patientId);
		LOG.debug("Ids results -> {}", result);
		return ResponseEntity.created(new URI("")).body(result);
	}
}
