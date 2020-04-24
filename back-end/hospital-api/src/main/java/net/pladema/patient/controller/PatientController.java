package net.pladema.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.service.AddressExternalService;
import net.pladema.patient.controller.dto.*;
import net.pladema.patient.controller.mapper.PatientMapper;
import net.pladema.patient.controller.mocks.MocksPatient;
import net.pladema.patient.repository.domain.BasicListedPatient;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.service.PersonExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/patient")
@Api(value = "Patient", tags = { "Patient" })
public class PatientController {

	private static final Logger LOG = LoggerFactory.getLogger(PatientController.class);

	private final PatientService patientService;

	private final PersonExternalService personExternalService;

	private final AddressExternalService addressExternalService;
	
	private final PatientMapper patientMapper;

	private final ObjectMapper jackson;
	
	public PatientController(PatientService patientService, PersonExternalService personExternalService, AddressExternalService addressExternalService,
							 PatientMapper patientMapper, ObjectMapper jackson) {
		this.patientService = patientService;
		this.personExternalService = personExternalService;
		this.addressExternalService = addressExternalService;
		this.jackson = jackson;
		this.patientMapper = patientMapper;
	}

	@GetMapping(value = "/search")
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
		Patient patientToAdd =  patientMapper.fromPatientDto(patientDto);
		patientToAdd.setPersonId(createdPerson.getId());
		Patient createdPatient = patientService.addPatient(patientToAdd);
		return ResponseEntity.created(new URI("")).body(patientMapper.fromPatient(createdPatient));
	}

	@GetMapping(value = "/minimalsearch")
	public ResponseEntity<List<Integer>> getPatientMinimal(
			@RequestParam(value = "identificationTypeId", required = true) Short identificationTypeId,
			@RequestParam(value = "identificationNumber"  , required = true) String identificationNumber,
			@RequestParam(value = "genderId", required = true) Short genderId) {
		LOG.debug("Input data -> {}", identificationTypeId, identificationNumber, genderId);
		List<Integer> result = personExternalService.getPersonByDniAndGender(identificationTypeId, identificationNumber, genderId);
		LOG.debug("Ids resultantes -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{patientId}/basicdata")
	public ResponseEntity<BasicPatientDto> getBasicDataPatient(@PathVariable(name = "patientId") Integer patientId){
		LOG.debug("Input parameters -> {}", patientId);
		BasicPatientDto result;
		try {
			Patient patient = patientService.getPatient(patientId)
					.orElseThrow(() -> new EntityNotFoundException("patient.invalid"));
			BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
			result = new BasicPatientDto(patient.getId(), personData);
		} catch (Exception e) {
			result = MocksPatient.mockBasicPatientDto(patientId);
		}
		LOG.debug("Output -> {}", result);
		return  ResponseEntity.ok().body(result);
	}

	@GetMapping("/basicdata")
	public ResponseEntity<List<BMPatientDto>> getAllPatientsData(){
		List<BasicListedPatient> patients = patientService.getPatients();
		List<BMPatientDto> result = patientMapper.fromBasicListedPatientList(patients);
		if (result.isEmpty())
			result.addAll(MocksPatient.mockBasicListedPatientsList());
		return  ResponseEntity.ok().body(result);
	}
}