package net.pladema.patient.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.service.AddressExternalService;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.controller.dto.BMPatientDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.person.controller.mapper.PersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import net.pladema.patient.controller.dto.PatientSearchDto;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.controller.mapper.PatientMapper;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientSearch;

@RestController
@RequestMapping("/patient")
@Api(value = "Patient", tags = { "Patient" })
public class PatientController {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final PatientService patientService;

	private final PersonExternalService personExternalService;

	private final AddressExternalService addressExternalService;
	
	private final PatientMapper patientMapper;

	private final ObjectMapper jackson;
	
	public PatientController(PatientService patientService, PersonExternalService personExternalService, AddressExternalService addressExternalService, ObjectMapper jackson, PatientMapper patientMapper, PersonMapper personMapper) {
		this.patientService = patientService;
		this.personExternalService = personExternalService;
		this.addressExternalService = addressExternalService;
		this.jackson = jackson;
		this.patientMapper = patientMapper;
	}

	@RequestMapping(value = "/search")
	public ResponseEntity<List<PatientSearchDto>> searchPatient(@RequestParam String searchFilterStr){
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
	public ResponseEntity<BMPatientDto> addPatient(
			@RequestBody APatientDto patientDto) throws URISyntaxException {

		BMPersonDto createdPerson = personExternalService.addPerson(patientDto);

		AddressDto addressToAdd = patientMapper.updatePatientAddress(patientDto);
		LOG.debug("Going to add address -> {}", addressToAdd);
		addressToAdd = addressExternalService.addAddress(addressToAdd);
		personExternalService.addPersonExtended(patientDto, createdPerson.getId(), addressToAdd.getId());
		return ResponseEntity.created(new URI("")).body(patientMapper.fromPerson(createdPerson));
	}

	@GetMapping(value = "/minimalsearch")
	public ResponseEntity<List<Integer>> getPatientMinimal(
			@RequestParam(value = "identificationTypeId", required = true) Short identificationTypeId,
			@RequestParam(value = "identificationNumber"  , required = true) String identificationNumber,
			@RequestParam(value = "genderId", required = true) Short genderId){
		LOG.debug("Input data -> {}", identificationTypeId, identificationNumber, genderId);
		List<Integer> result = personExternalService.getPersonByDniAndGender(identificationTypeId,identificationNumber, genderId);
		LOG.debug("Ids resultantes -> {}", result);
		return  ResponseEntity.ok().body(result);
	}
}