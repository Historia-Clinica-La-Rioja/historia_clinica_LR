package net.pladema.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.service.AddressExternalService;
import net.pladema.patient.controller.constraints.FilterValid;
import net.pladema.patient.controller.dto.*;
import net.pladema.patient.controller.mapper.PatientMapper;
import net.pladema.patient.repository.PatientTypeRepository;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.controller.service.PersonExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/patient")
@Api(value = "Patient", tags = { "Patient" })
@Validated
public class PatientController {

	private static final Logger LOG = LoggerFactory.getLogger(PatientController.class);

	public static final String OUTPUT = "Output -> {}";

	private final PatientService patientService;

	private final PatientTypeRepository patientTypeRepository;

	private final PersonExternalService personExternalService;

	private final AddressExternalService addressExternalService;

	private final PatientMapper patientMapper;

	private final PersonMapper personMapper;

	private final ObjectMapper jackson;

	public PatientController(PatientService patientService, PersonExternalService personExternalService,
			AddressExternalService addressExternalService, PatientMapper patientMapper, PersonMapper personMapper,
			ObjectMapper jackson, PatientTypeRepository patientTypeRepository) {
		this.patientService = patientService;
		this.personExternalService = personExternalService;
		this.addressExternalService = addressExternalService;
		this.jackson = jackson;
		this.patientMapper = patientMapper;
		this.patientTypeRepository = patientTypeRepository;
		this.personMapper = personMapper;
	}

	@GetMapping(value = "/search")
	public ResponseEntity<List<PatientSearchDto>> searchPatient(@RequestParam @FilterValid String searchFilterStr) {
		LOG.debug("Input data -> searchFilterStr {} ", searchFilterStr);
		PatientSearchFilter searchFilter = null;
		try {
			searchFilter = jackson.readValue(searchFilterStr, PatientSearchFilter.class);
		} catch (IOException e) {
			LOG.error(String.format("Error mappeando filter: %s", searchFilterStr), e);
		}
		List<PatientSearch> resultado = patientService.searchPatient(searchFilter);

		return ResponseEntity.ok(patientMapper.fromListPatientSearch(resultado));
	}

	@PostMapping
	@Transactional
	public ResponseEntity<BMPatientDto> addPatient(@RequestBody APatientDto patientDto) throws URISyntaxException {
		LOG.debug("Input data -> APatientDto {} ", patientDto);
		BMPersonDto createdPerson = personExternalService.addPerson(patientDto);

		AddressDto addressToAdd = patientMapper.updatePatientAddress(patientDto);
		LOG.debug("Going to add address -> {}", addressToAdd);
		addressToAdd = addressExternalService.addAddress(addressToAdd);
		personExternalService.addPersonExtended(patientDto, createdPerson.getId(), addressToAdd.getId());
		Patient patientToAdd = patientMapper.fromPatientDto(patientDto);
		patientToAdd.setPersonId(createdPerson.getId());
		Patient createdPatient = patientService.addPatient(patientToAdd);
		if (createdPatient.isValidated()) {
			patientService.federatePatient(createdPatient, personMapper.fromPersonDto(createdPerson));
		}
		return ResponseEntity.created(new URI("")).body(patientMapper.fromPatient(createdPatient));
	}

	@GetMapping(value = "/minimalsearch")
	public ResponseEntity<List<Integer>> getPatientMinimal(
			@RequestParam(value = "identificationTypeId", required = true) Short identificationTypeId,
			@RequestParam(value = "identificationNumber", required = true) String identificationNumber,
			@RequestParam(value = "genderId", required = true) Short genderId) {
		LOG.debug("Input data -> identificationTypeId {}, identificationNumber {}, genderId {}", identificationTypeId,
				identificationNumber, genderId);
		List<Integer> result = personExternalService.getPersonByDniAndGender(identificationTypeId, identificationNumber,
				genderId);
		LOG.debug("Ids results -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{patientId}/basicdata")
	public ResponseEntity<BasicPatientDto> getBasicDataPatient(@PathVariable(name = "patientId") Integer patientId) {
		LOG.debug("Input parameters -> patientId {}", patientId);

		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException("patient.invalid"));
		BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
		BasicPatientDto result = new BasicPatientDto(patient.getId(), personData);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{patientId}/completedata")
	public ResponseEntity<CompletePatientDto> getCompleteDataPatient(
			@PathVariable(name = "patientId") Integer patientId) {
		LOG.debug("Input parameters -> patientId {}", patientId);
		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException("patient.invalid"));
		BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
		PatientType patientType = patientTypeRepository.getOne(patient.getTypeId());
		CompletePatientDto result = new CompletePatientDto(patient, patientType, personData);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}
}