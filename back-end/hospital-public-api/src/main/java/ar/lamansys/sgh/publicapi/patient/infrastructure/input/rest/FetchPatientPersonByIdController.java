package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.FetchPatientPersonById;
import ar.lamansys.sgh.publicapi.patient.application.exception.PatientNotExistsException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest.mapper.FetchPatientPersonByIdMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.person.PersonDto;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/public-api/patient/{patientId}")
@Tag(name = "PublicApi Pacientes", description = "External patient Api")
public class FetchPatientPersonByIdController {

	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";
	private static final Logger LOG = LoggerFactory.getLogger(ExternalPatientController.class);
	private final FetchPatientPersonById fetchPatientPersonById;

	public FetchPatientPersonByIdController(FetchPatientPersonById fetchPatientPersonById) {
		this.fetchPatientPersonById = fetchPatientPersonById;
	}

	@GetMapping
	public PersonDto getPersonInfo(
			@PathVariable("patientId") String patientId) throws PatientNotExistsException{
		LOG.debug(INPUT + "patientId {}", patientId);
		PersonDto result = FetchPatientPersonByIdMapper.fromBo(fetchPatientPersonById.run(patientId));
		log.debug(OUTPUT, result);
		return result;
	}

}
