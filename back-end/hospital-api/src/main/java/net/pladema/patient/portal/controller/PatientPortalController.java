package net.pladema.patient.portal.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.controller.dto.HCEAllergyDto;
import net.pladema.clinichistory.documents.controller.dto.HCEAnthropometricDataDto;
import net.pladema.clinichistory.documents.controller.dto.HCELast2VitalSignsDto;
import net.pladema.clinichistory.documents.controller.dto.HCEMedicationDto;
import net.pladema.clinichistory.documents.controller.dto.HCEPersonalHistoryDto;
import net.pladema.clinichistory.documents.controller.service.HCEAllergyExternalService;
import net.pladema.clinichistory.documents.controller.service.HCEClinicalObservationExternalService;
import net.pladema.clinichistory.documents.controller.service.HCEHealthConditionsExternalService;
import net.pladema.clinichistory.documents.controller.service.HCEMedicationExternalService;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.portal.service.PatientPortalService;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.PatientService;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.dto.PersonPhotoDto;
import net.pladema.person.controller.dto.PersonalInformationDto;
import net.pladema.person.controller.service.PersonExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/patientportal")
@Api(value = "Patient Portal", tags = { "Patient Portal" })
@Validated
public class PatientPortalController {

	private static final Logger LOG = LoggerFactory.getLogger(PatientPortalController.class);

	private static final String LOGGING_OUTPUT = "Output -> {}";

	public static final String PATIENT_INVALID = "patient.invalid";

	private final HCEHealthConditionsExternalService hceHealthConditionsExternalService;

	private final HCEMedicationExternalService hceMedicationExternalService;

	private final HCEAllergyExternalService hceAllergyExternalService;

	private final HCEClinicalObservationExternalService hceClinicalObservationExternalService;

	private final PatientPortalService patientPortalService;

	private final PersonExternalService personExternalService;

	private final PatientService patientService;

	public PatientPortalController(HCEHealthConditionsExternalService hceHealthConditionsExternalService, HCEMedicationExternalService hceMedicationExternalService,
								   HCEAllergyExternalService hceAllergyExternalService, HCEClinicalObservationExternalService hceClinicalObservationExternalService, PatientPortalService patientPortalService, PersonExternalService personExternalService, PatientService patientService){
		this.hceHealthConditionsExternalService = hceHealthConditionsExternalService;
		this.hceMedicationExternalService = hceMedicationExternalService;
		this.hceAllergyExternalService = hceAllergyExternalService;
		this.hceClinicalObservationExternalService = hceClinicalObservationExternalService;
		this.patientPortalService = patientPortalService;
		this.personExternalService = personExternalService;
		this.patientService = patientService;
	}

	@GetMapping("/medications")
	public ResponseEntity<List<HCEMedicationDto>> getMedications(){
		Integer patientId = patientPortalService.getPatientId();
		List<HCEMedicationDto> result = hceMedicationExternalService.getMedication(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/allergies")
	public ResponseEntity<List<HCEAllergyDto>> getAllergies(){
		Integer patientId = patientPortalService.getPatientId();
		List<HCEAllergyDto> result = hceAllergyExternalService.getAllergies(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/anthropometricData")
	public ResponseEntity<HCEAnthropometricDataDto> getAnthropometricData(){
		Integer patientId = patientPortalService.getPatientId();
		HCEAnthropometricDataDto result = hceClinicalObservationExternalService.getLastAnthropometricDataGeneralState(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/vitalSigns")
	public ResponseEntity<HCELast2VitalSignsDto> getVitalSigns(){
		Integer patientId = patientPortalService.getPatientId();
		HCELast2VitalSignsDto result = hceClinicalObservationExternalService.getLast2VitalSignsGeneralState(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/familyHistories")
	public ResponseEntity<List<HCEPersonalHistoryDto>> getFamilyHistories(){
		Integer patientId = patientPortalService.getPatientId();
		List<HCEPersonalHistoryDto> result = hceHealthConditionsExternalService.getFamilyHistories(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/personalHistories")
	public ResponseEntity<List<HCEPersonalHistoryDto>> getPersonalHistories(){
		Integer patientId = patientPortalService.getPatientId();
		List<HCEPersonalHistoryDto> result = hceHealthConditionsExternalService.getActivePersonalHistories(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/personalInformation")
	public ResponseEntity<PersonalInformationDto> getPersonalInformation(){
		Integer personId = patientPortalService.getPersonId();
		PersonalInformationDto result = personExternalService.getPersonalInformation(personId);
		LOG.debug(LOGGING_OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}

	@GetMapping("/basicdata")
	public ResponseEntity<BasicPatientDto> getBasicDataPatient() {
		Integer patientId = patientPortalService.getPatientId();
		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
		BasicPatientDto result = new BasicPatientDto(patient.getId(), personData, patient.getTypeId());
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/photo")
	public ResponseEntity<PersonPhotoDto> getPatientPhoto() {
		Integer patientId = patientPortalService.getPatientId();
		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		PersonPhotoDto personPhotoDto = personExternalService.getPersonPhoto(patient.getPersonId());
		LOG.debug(LOGGING_OUTPUT, personPhotoDto);
		return ResponseEntity.ok().body(personPhotoDto);
	}
}
