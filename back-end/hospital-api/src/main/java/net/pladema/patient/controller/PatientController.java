package net.pladema.patient.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.PatientGenderAgeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EAuditType;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.service.AddressExternalService;
import net.pladema.audit.service.domain.enums.EActionType;
import net.pladema.federar.controller.FederarExternalService;
import net.pladema.federar.services.domain.FederarResourceAttributes;
import net.pladema.patient.controller.constraints.PatientUpdateValid;
import net.pladema.patient.controller.dto.AAdditionalDoctorDto;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.controller.dto.CompletePatientDto;
import net.pladema.patient.controller.dto.LimitedPatientSearchDto;
import net.pladema.patient.controller.dto.PatientLastEditInfoDto;
import net.pladema.patient.controller.dto.PatientPhotoDto;
import net.pladema.patient.controller.dto.PatientSearchDto;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.controller.dto.ReducedPatientDto;
import net.pladema.patient.controller.mapper.PatientMapper;
import net.pladema.patient.repository.PatientTypeRepository;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.AdditionalDoctorService;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.DoctorsBo;
import net.pladema.patient.service.domain.LimitedPatientSearchBo;
import net.pladema.patient.service.domain.PatientGenderAgeBo;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicPersonalDataDto;
import net.pladema.person.controller.dto.PersonPhotoDto;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.user.application.getrolesbyuser.GetRolesByUser;
import net.pladema.user.application.port.HospitalUserStorage;
import net.pladema.user.infrastructure.input.rest.dto.UserRoleDto;
import net.pladema.user.infrastructure.input.rest.mapper.HospitalUserRoleMapper;

@RestController
@RequestMapping("/patient")
@Tag(name = "Patient", description = "Patient")
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

	private final FederarExternalService federarExternalService;

	private final GetRolesByUser getRolesByUser;

	private final HospitalUserRoleMapper hospitalUserRoleMapper;

	private final HospitalUserStorage hospitalUserStorage;

	private static final long errorRangeTime = 100;

	private final static Integer NO_INSTITUTION = -1;

	public PatientController(PatientService patientService, PersonExternalService personExternalService,
							 AddressExternalService addressExternalService, PatientMapper patientMapper, PersonMapper personMapper,
							 ObjectMapper jackson, PatientTypeRepository patientTypeRepository, AdditionalDoctorService additionalDoctorService,
							 FederarExternalService federarExternalService, GetRolesByUser getRolesByUser,
							 HospitalUserRoleMapper hospitalUserRoleMapper, HospitalUserStorage hospitalUserStorage) {
		this.patientService = patientService;
		this.personExternalService = personExternalService;
		this.addressExternalService = addressExternalService;
		this.jackson = jackson;
		this.patientMapper = patientMapper;
		this.patientTypeRepository = patientTypeRepository;
		this.personMapper = personMapper;
		this.additionalDoctorService = additionalDoctorService;
		this.federarExternalService = federarExternalService;
		this.getRolesByUser = getRolesByUser;
		this.hospitalUserRoleMapper = hospitalUserRoleMapper;
		this.hospitalUserStorage = hospitalUserStorage;
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
	public ResponseEntity<LimitedPatientSearchDto> searchPatientOptionalFilters(@RequestParam String searchFilterStr) {
		LOG.debug("Input data -> searchFilterStr {} ", searchFilterStr);
		PatientSearchFilter searchFilter = null;
		try {
			searchFilter = jackson.readValue(searchFilterStr, PatientSearchFilter.class);
		} catch (IOException e) {
			LOG.error(String.format("Error mappeando filter: %s", searchFilterStr), e);
		}
		LimitedPatientSearchBo limitedPatientSearchBo = patientService.searchPatientOptionalFilters(searchFilter);
		LimitedPatientSearchDto result = mapToLimitedPatientSearchDto(limitedPatientSearchBo);

		return ResponseEntity.ok(result);
	}

	@PostMapping(value = "/institution/{institutionId}")
	@Transactional
	public ResponseEntity<Integer> addPatient(@RequestBody APatientDto patientDto,
											  @PathVariable(name = "institutionId") Integer institutionId) throws URISyntaxException {
		LOG.debug("Input data -> APatientDto {} ", patientDto);
		BMPersonDto createdPerson = personExternalService.addPerson(patientDto);
		AddressDto addressToAdd = persistPatientAddress(patientDto, Optional.empty());
		personExternalService.addPersonExtended(patientDto, createdPerson.getId(), addressToAdd.getId());
		Patient patient = persistPatientData(patientDto, createdPerson, null, institutionId);
		Patient createdPatient = clonePatient(patient);
		if (createdPatient.isValidated()) {
			Person person = personMapper.fromPersonDto(createdPerson);

			FederarResourceAttributes attributes = new FederarResourceAttributes();
			BeanUtils.copyProperties(person, attributes);
			try {
				federarExternalService.federatePatient(attributes, createdPatient.getId()).ifPresent(
						nationalId -> {
							createdPatient.setNationalId(nationalId);
							createdPatient.setTypeId(PatientType.PERMANENT);
							patientService.addPatient(createdPatient);
							LOG.debug("Successful federated patient with nationalId => {}", nationalId);
						}
				);
			}
			catch (Exception ex){
				LOG.error("Fallo en la comunicación => {}", ex.getMessage());
			}
		}

		patientService.auditActionPatient(institutionId, createdPatient.getId(), EActionType.CREATE);

		return ResponseEntity.created(new URI("")).body(createdPatient.getId());
	}

	private Patient clonePatient(Patient p){
		Patient result =  new Patient();
		result.setId(p.getId());
		result.setPersonId(p.getPersonId());
		result.setTypeId(p.getTypeId());
		result.setPossibleDuplicate(p.getPossibleDuplicate());
		result.setNationalId(p.getNationalId());
		result.setComments(p.getComments());
		result.setIdentityVerificationStatusId(p.getIdentityVerificationStatusId());
		result.setAuditTypeId(p.getAuditTypeId());
		result.setCreatedOn(p.getCreatedOn());
		result.setCreatedBy(p.getCreatedBy());
		result.setUpdatedOn(p.getUpdatedOn());
		result.setUpdatedBy(p.getUpdatedBy());
		result.setDeleted(p.isDeleted());
		if(p.getDeleteable()!=null){
			result.setDeletedOn(p.getDeletedOn());
			result.setDeletedBy(p.getDeletedBy());
		}
		return result;
	}


	@PutMapping(value = "/{patientId}/institution/{institutionId}")
	@Transactional
	@PatientUpdateValid
	public ResponseEntity<Integer> updatePatient(@PathVariable(name = "patientId") Integer patientId,
												 @PathVariable(name = "institutionId") Integer institutionId,
												 @RequestBody APatientDto patientDto) throws URISyntaxException {
		LOG.debug("Input data -> APatientDto {} ", patientDto);
		Patient patient = patientService.getPatient(patientId).orElseThrow(() -> new NotFoundException("patient-not-found", "Patient not found"));
		if(!patient.getTypeId().equals(EPatientType.REJECTED.getId()) && patientDto.getTypeId().equals(EPatientType.REJECTED.getId())){
			patientService.assertHasActiveEncountersByPatientId(patientId);
			hospitalUserStorage.getUserDataByPersonId(patient.getPersonId())
					.ifPresent(u-> hospitalUserStorage.disableUser(u.getId()));
		}
		BMPersonDto createdPerson = personExternalService.updatePerson(patientDto, patient.getPersonId());

		PersonExtended personExtendedUpdated = personExternalService.updatePersonExtended(patientDto,
				createdPerson.getId());

		var addressId = personExtendedUpdated.getAddressId();

		if(addressId == null) {
			AddressDto addressDto =  persistPatientAddress(patientDto, Optional.empty());
			addressId = addressExternalService.addAddress(addressDto).getId();
			personExternalService.addPersonExtended(patientDto, createdPerson.getId(), addressId);
		}

		persistPatientAddress(patientDto, Optional.of(addressId));
		Patient createdPatient = persistPatientData(patientDto, createdPerson, patient, institutionId);

		if (!Objects.equals(institutionId, NO_INSTITUTION)) {
			patientService.auditActionPatient(institutionId,patientId, EActionType.UPDATE);
			if (patientDto.getAuditType() != null && patientDto.getAuditType().equals(EAuditType.TO_AUDIT))
				patientService.persistSelectionForAnAudict(patientId, institutionId, patientDto.getMessage());
		}
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

		Patient patient = patientService.getActivePatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		BasicPatientDto result = new BasicPatientDto(patient.getId(), patient.getTypeId());
		if (patient.getPersonId() != null) {
			BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
			result.setPerson(personData);
		}
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


	@GetMapping(value= "/photos", params = "patientsIds")
	public ResponseEntity<List<PatientPhotoDto>> getPatientPhotos(@RequestParam List<Integer> patientsIds) {
		LOG.debug("Input parameters -> {}", patientsIds);

		List<Patient> patients = patientService.getPatients(Set.copyOf(patientsIds));
		List<Integer> personsIds = getPersonsIds(patients);

		List<PersonPhotoDto> personsPhotos = personExternalService.getPersonsPhotos(personsIds);
		List<PatientPhotoDto> patientsPhotos = new ArrayList<>();
		patients.forEach(p -> {
			Optional<PersonPhotoDto> personPhotoDto = findPhoto(p.getPersonId(), personsPhotos);
			personPhotoDto.ifPresent(pp -> {
				PatientPhotoDto patientPhotoDto = new PatientPhotoDto(p.getId(), pp.getImageData());
				patientsPhotos.add(patientPhotoDto);
			});
		});

		LOG.debug("Output size -> {}", patientsPhotos.size());
		LOG.trace(OUTPUT, patientsPhotos);
		return ResponseEntity.ok().body(patientsPhotos);
	}

	@PostMapping("/{patientId}/photo")
	public ResponseEntity<Boolean> addPatientPhoto(@PathVariable(name = "patientId") Integer patientId,
												   @RequestBody PersonPhotoDto personPhotoDto) {
		LOG.debug("Input parameters -> patientId {}, PersonPhotoDto {}", patientId, personPhotoDto);
		Patient patient = patientService.getActivePatient(patientId)
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
		PatientType patientType = patientTypeRepository.findById(patient.getTypeId()).get();
		CompletePatientDto result = new CompletePatientDto(patient, patientType, personData,
				doctorsBo.getGeneralPractitionerBo() != null
						? new AAdditionalDoctorDto(doctorsBo.getGeneralPractitionerBo())
						: null,
				doctorsBo.getPamiDoctorBo() != null ? new AAdditionalDoctorDto(doctorsBo.getPamiDoctorBo()) : null);
		var lastEditOpt = hospitalUserStorage.getUserPersonInfo(patient.getUpdatedBy());

		if (lastEditOpt.isPresent()) {
			var lastEdit = lastEditOpt.get();
			String name = lastEdit.getFullName();
			if (!(name == null || name.isBlank())) {
				if (patient.getUpdatedOn().isBefore(patient.getCreatedOn().plus(errorRangeTime,ChronoUnit.MILLIS)))
					result.setPatientLastEditInfoDto(new PatientLastEditInfoDto(patient.getUpdatedOn(), lastEdit.getFullName(), false));
				else
					result.setPatientLastEditInfoDto(new PatientLastEditInfoDto(patient.getUpdatedOn(), lastEdit.getFullName(), true));
			}
		}
		if(patient.getAuditTypeId().equals(EAuditType.TO_AUDIT.getId()))
			result.setAuditablePatientInfo(patientService.getAuditablePatientInfo(patientId));
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{patientId}/appointment-patient-data")
	public ResponseEntity<ReducedPatientDto> getBasicPersonalData(@PathVariable(name = "patientId") Integer patientId) {
		LOG.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
		Patient patient = patientService.getActivePatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		BasicPersonalDataDto personData = personExternalService.getBasicPersonalDataDto(patient.getPersonId());
		ReducedPatientDto result = new ReducedPatientDto(personData, patient.getTypeId());
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{patientId}/gender-age")
	public ResponseEntity<PatientGenderAgeDto> getPatientGenderAge(@PathVariable(name = "patientId") Integer patientId){
		LOG.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
		PatientGenderAgeBo patientGenderAgeBo = patientService.getPatientGenderAge(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		PatientGenderAgeDto result = patientMapper.toPatientGenderAgeDto(patientGenderAgeBo);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	private AddressDto persistPatientAddress(APatientDto patientDto, Optional<Integer> idAdress) {
		AddressDto addressToAdd = patientMapper.updatePatientAddress(patientDto);
		LOG.debug("Going to add address -> {}", addressToAdd);
		idAdress.ifPresent(addressToAdd::setId);
		return addressExternalService.addAddress(addressToAdd);
	}

	private Patient persistPatientData(APatientDto patientDto, BMPersonDto createdPerson, Patient patientEntity, Integer institutionId) {
		Patient patientToAdd = patientMapper.fromPatientDto(patientDto);
		patientToAdd.setPersonId(createdPerson.getId());
		if (patientEntity != null) {
			setPatientData(patientToAdd, patientEntity);
			setPatientType(patientToAdd, patientEntity, institutionId);
		}
		Patient createdPatient = patientService.addPatient(patientToAdd);
		if (patientDto.getGeneralPractitioner() != null && patientDto.getPamiDoctor() != null) {
			DoctorsBo doctorsBo = new DoctorsBo(patientDto.getGeneralPractitioner(), patientDto.getPamiDoctor());
			additionalDoctorService.addAdditionalDoctors(doctorsBo, createdPatient.getId());
		}
		LOG.debug(OUTPUT, createdPatient.getId());
		return createdPatient;
	}

	private boolean usuarioTieneRol(Integer institutionId) {
		List<UserRoleDto> auditorRole = hospitalUserRoleMapper.toListUserRoleDto(getRolesByUser.execute(UserInfo.getCurrentAuditor(), institutionId))
				.stream().filter(a -> a.getRoleId() == ERole.AUDITOR_MPI.getId()).collect(Collectors.toList());

		return !auditorRole.isEmpty();
	}

	private void setPatientType(Patient patientToAdd, Patient patientHistory, Integer institutionId) {
		if (!usuarioTieneRol(institutionId))
			patientToAdd.setTypeId(patientHistory.getTypeId());
	}

	private void setPatientData (Patient patientToAdd, Patient patientHistory) {
		if (patientToAdd.getAuditTypeId() == null)
			patientToAdd.setAuditTypeId(patientHistory.getAuditTypeId());
		patientToAdd.setId(patientHistory.getId());
		patientToAdd.setIdentityVerificationStatusId(patientHistory.getIdentityVerificationStatusId());
		patientToAdd.setComments(patientHistory.getComments());
		patientToAdd.setNationalId(patientHistory.getNationalId());
		patientToAdd.setCreatedBy(patientHistory.getCreatedBy());
		patientToAdd.setCreatedOn(patientHistory.getCreatedOn());
		patientToAdd.setUpdatedBy(UserInfo.getCurrentAuditor());
		patientToAdd.setUpdatedOn(LocalDateTime.now());
		patientToAdd.setDeletedBy(patientHistory.getDeletedBy());
		patientToAdd.setDeletedOn(patientHistory.getDeletedOn());
		patientToAdd.setDeleted(patientHistory.getDeleteable().getDeleted());
	}

	private List<Integer> getPersonsIds(List<Patient> patients) {
		LOG.debug("Input size -> {}", patients.size());
		LOG.trace("Input parameters -> patients {}", patients);

		List<Integer> personsIds = patients
				.stream()
				.map(Patient::getPersonId)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		LOG.debug("Output size -> {}", personsIds.size());
		LOG.trace(OUTPUT, personsIds);
		return personsIds;
	}

	private Optional<PersonPhotoDto> findPhoto(Integer personId, List<PersonPhotoDto> personsPhotos) {
		LOG.debug("Input parameters -> personId {}, personsPhotosSize {}", personId, personsPhotos.size());
		LOG.trace("Input parameters -> personId {}, personsPhotos {}", personId, personsPhotos);

		Optional<PersonPhotoDto> optPhoto = personsPhotos
				.stream()
				.filter(p -> personId.equals(p.getPersonId()))
				.findFirst();

		LOG.debug("Output -> {}", optPhoto);
		return optPhoto;
	}

	private LimitedPatientSearchDto mapToLimitedPatientSearchDto(LimitedPatientSearchBo limitedPatientSearchBo) {
		LimitedPatientSearchDto result = this.patientMapper.toLimitedPatientSearchDto(limitedPatientSearchBo);
		result.getPatientList().stream().forEach(p -> {
			limitedPatientSearchBo.getPatientList().stream().forEach( pl -> {
				if(pl.getIdPatient().equals(p.getIdPatient())) {
					p.getPerson().setNameSelfDetermination(pl.getNameSelfDetermination());
				}
			});
		});
		return result;
	}

}