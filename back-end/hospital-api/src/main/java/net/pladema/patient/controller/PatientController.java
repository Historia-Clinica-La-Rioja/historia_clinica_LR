package net.pladema.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.service.AddressExternalService;
import net.pladema.patient.controller.constraints.PatientUpdateValid;
import net.pladema.patient.controller.dto.*;
import net.pladema.patient.controller.mapper.PatientMapper;
import net.pladema.patient.repository.PatientTypeRepository;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.AdditionalDoctorService;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.DoctorsBo;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.dto.PersonPhotoDto;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.person.controller.dto.BasicPersonalDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
@RequestMapping("/patient")
@Api(value = "Patient", tags = { "Patient" })
@Validated
public class PatientController {

	private static final Logger LOG = LoggerFactory.getLogger(PatientController.class);

	public static final String OUTPUT = "Output -> {}";
	public static final String PATIENT_INVALID = "patient.invalid";
	public static final String INPUT_PARAMETERS_PATIENT_ID = "Input parameters -> patientId {}";

	private final PatientService patientService;

	private final PatientTypeRepository patientTypeRepository;

	private final PersonExternalService personExternalService;

	private final AddressExternalService addressExternalService;

	private final AdditionalDoctorService additionalDoctorService;

	private final PatientMapper patientMapper;

	private final PersonMapper personMapper;

	private final ObjectMapper jackson;

	public PatientController(PatientService patientService, PersonExternalService personExternalService,
							 AddressExternalService addressExternalService, PatientMapper patientMapper, PersonMapper personMapper,
							 ObjectMapper jackson, PatientTypeRepository patientTypeRepository,AdditionalDoctorService additionalDoctorService) {
		this.patientService = patientService;
		this.personExternalService = personExternalService;
		this.addressExternalService = addressExternalService;
		this.jackson = jackson;
		this.patientMapper = patientMapper;
		this.patientTypeRepository = patientTypeRepository;
		this.personMapper = personMapper;
		this.additionalDoctorService = additionalDoctorService;
	}

	@GetMapping(value = "/search")
	public ResponseEntity<List<PatientSearchDto>> searchPatient(@RequestParam String searchFilterStr) {
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

	@GetMapping(value = "/optionalfilter")
	public ResponseEntity<List<PatientSearchDto>> searchPatientOptionalFilters(@RequestParam String searchFilterStr) {
		LOG.debug("Input data -> searchFilterStr {} ", searchFilterStr);
		PatientSearchFilter searchFilter = null;
		try {
			searchFilter = jackson.readValue(searchFilterStr, PatientSearchFilter.class);
		} catch (IOException e) {
			LOG.error(String.format("Error mappeando filter: %s", searchFilterStr), e);
		}
		List<PatientSearch> resultado = patientService.searchPatientOptionalFilters(searchFilter);

		return ResponseEntity.ok(patientMapper.fromListPatientSearch(resultado));
	}

	@PostMapping
	@Transactional
	public ResponseEntity<Integer> addPatient(@RequestBody APatientDto patientDto) throws URISyntaxException {
		LOG.debug("Input data -> APatientDto {} ", patientDto);
		BMPersonDto createdPerson = personExternalService.addPerson(patientDto);
		AddressDto addressToAdd = persistPatientAddress(patientDto, Optional.empty());
		personExternalService.addPersonExtended(patientDto, createdPerson.getId(), addressToAdd.getId());
		Patient createdPatient = persistPatientData(patientDto, createdPerson, patient->{});
		if (createdPatient.isValidated()) {
			patientService.federatePatient(createdPatient, personMapper.fromPersonDto(createdPerson));
		}
		return ResponseEntity.created(new URI("")).body(createdPatient.getId());
	}

	

	@PutMapping(value = "/{patientId}")
	@Transactional
	@PatientUpdateValid
	public ResponseEntity<Integer> updatePatient(@PathVariable(name = "patientId") Integer patientId,
			@RequestBody APatientDto patientDto) throws URISyntaxException {
		LOG.debug("Input data -> APatientDto {} ", patientDto);
		Patient patient = patientService.getPatient(patientId).orElseThrow(()->new NotFoundException("patient-not-found", "Patient not found"));
		BMPersonDto createdPerson = personExternalService.updatePerson(patientDto, patient.getPersonId());
		PersonExtended personExtendedUpdated = personExternalService.updatePersonExtended(patientDto,
				createdPerson.getId());
		persistPatientAddress(patientDto, Optional.of(personExtendedUpdated.getAddressId()));
		Patient createdPatient = persistPatientData(patientDto, createdPerson, (Patient pat) -> pat.setId(patientId));
		return ResponseEntity.created(new URI("")).body(createdPatient.getId());
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
		LOG.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);

		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
		BasicPatientDto result = new BasicPatientDto(patient.getId(), personData,patient.getTypeId());
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{patientId}/photo")
	public ResponseEntity<PersonPhotoDto> getPatientPhoto(@PathVariable(name = "patientId") Integer patientId) {
		LOG.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		PersonPhotoDto personPhotoDto = personExternalService.getPersonPhoto(patient.getPersonId());
		LOG.debug(OUTPUT, personPhotoDto);
		return ResponseEntity.ok().body(personPhotoDto);
	}

	@PostMapping("/{patientId}/photo")
	@Transactional
	public ResponseEntity<Boolean> addPatientPhoto(@PathVariable(name = "patientId") Integer patientId,
														   @RequestBody PersonPhotoDto personPhotoDto) {
		LOG.debug("Input parameters -> patientId {}, PersonPhotoDto {}", patientId, personPhotoDto);
		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		boolean result = personExternalService.savePersonPhoto(patient.getPersonId(), personPhotoDto.getImageData());
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{patientId}/completedata")
	public ResponseEntity<CompletePatientDto> getCompleteDataPatient(
			@PathVariable(name = "patientId") Integer patientId) {
		LOG.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		DoctorsBo doctorsBo = additionalDoctorService.getAdditionalDoctors(patientId);
		BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
		PatientType patientType = patientTypeRepository.getOne(patient.getTypeId());
		CompletePatientDto result = new CompletePatientDto(patient, patientType, personData,
				doctorsBo.getGeneralPractitionerBo() != null
						? new AAdditionalDoctorDto(doctorsBo.getGeneralPractitionerBo())
						: null,
				doctorsBo.getPamiDoctorBo() != null ? new AAdditionalDoctorDto(doctorsBo.getPamiDoctorBo()) : null);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}
	
	private AddressDto persistPatientAddress(APatientDto patientDto, Optional<Integer> idAdress) {
		AddressDto addressToAdd = patientMapper.updatePatientAddress(patientDto);
		LOG.debug("Going to add address -> {}", addressToAdd);
		idAdress.ifPresent(addressToAdd::setId);
		return addressExternalService.addAddress(addressToAdd);
	}

	private Patient persistPatientData(APatientDto patientDto, BMPersonDto createdPerson, Consumer<Patient> addIds) {
		Patient patientToAdd = patientMapper.fromPatientDto(patientDto);
		patientToAdd.setPersonId(createdPerson.getId());
		addIds.accept(patientToAdd);
		Patient createdPatient = patientService.addPatient(patientToAdd);
		DoctorsBo doctorsBo = new DoctorsBo(patientDto.getGeneralPractitioner(),patientDto.getPamiDoctor());
		additionalDoctorService.addAdditionalDoctors(doctorsBo,createdPatient.getId());
		LOG.debug(OUTPUT, createdPatient.getId());
		return createdPatient;
	}

	@GetMapping("/{patientId}/appointment-patient-data")
	public ResponseEntity<ReducedPatientDto> getBasicPersonalData(@PathVariable(name = "patientId") Integer patientId) {
		LOG.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		BasicPersonalDataDto personData = personExternalService.getBasicPersonalDataDto(patient.getPersonId());
		ReducedPatientDto result = new ReducedPatientDto(personData,patient.getTypeId());
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}
}