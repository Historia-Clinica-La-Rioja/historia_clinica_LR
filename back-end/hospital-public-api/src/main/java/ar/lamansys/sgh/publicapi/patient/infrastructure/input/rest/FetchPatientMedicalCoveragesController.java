package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.patient.application.exception.PatientNotExistsException;
import ar.lamansys.sgh.publicapi.patient.application.fetchmedicalcoverages.FetchPatientMedicalCoverages;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest.mapper.FetchPatientMedicalCoveragesMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientMedicalCoverageDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/public-api/patient/{patientId}/medical-coverages")
@Tag(name = "PublicApi Pacientes", description = "Patient Medical Coverages")
public class FetchPatientMedicalCoveragesController {

	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";
	private final FetchPatientMedicalCoverages fetchPatientMedicalCoverages;

	@GetMapping
	public List<SharedPatientMedicalCoverageDto> getPatientMedicalCoverages(
			@PathVariable("patientId") Integer patientId) throws PatientNotExistsException{
		log.debug(INPUT + "patientId {}", patientId);
		List<SharedPatientMedicalCoverageDto> result = fetchPatientMedicalCoverages.run(patientId).stream()
				.map(FetchPatientMedicalCoveragesMapper::fromBo)
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

}
