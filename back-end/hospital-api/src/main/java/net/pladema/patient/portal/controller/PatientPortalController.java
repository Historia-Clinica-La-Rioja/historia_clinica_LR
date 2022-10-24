package net.pladema.patient.portal.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEAllergyDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEAnthropometricDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCELast2RiskFactorsDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEMedicationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEPersonalHistoryDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service.HCEAllergyExternalService;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service.HCEClinicalObservationExternalService;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service.HCEHealthConditionsExternalService;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service.HCEMedicationExternalService;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.patient.controller.dto.AAdditionalDoctorDto;
import net.pladema.patient.controller.dto.CompletePatientDto;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.service.PatientExternalMedicalCoverageService;
import net.pladema.patient.portal.service.PatientPortalService;
import net.pladema.patient.repository.PatientTypeRepository;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.AdditionalDoctorService;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.DoctorsBo;
import net.pladema.person.controller.dto.PersonPhotoDto;
import net.pladema.person.controller.dto.PersonalInformationDto;
import net.pladema.person.controller.service.PersonExternalService;
import org.apache.http.MethodNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/patientportal")
@Tag(name = "Patient Portal", description = "Patient Portal")
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

	private final AdditionalDoctorService additionalDoctorService;

	private final PatientTypeRepository patientTypeRepository;

	private final PatientExternalMedicalCoverageService patientExternalMedicalCoverageService;

	private final FeatureFlagsService featureFlagsService;

	public PatientPortalController(HCEHealthConditionsExternalService hceHealthConditionsExternalService,
								   HCEMedicationExternalService hceMedicationExternalService,
								   HCEAllergyExternalService hceAllergyExternalService,
								   HCEClinicalObservationExternalService hceClinicalObservationExternalService,
								   PatientPortalService patientPortalService,
								   PersonExternalService personExternalService,
								   PatientService patientService,
								   AdditionalDoctorService additionalDoctorService,
								   PatientTypeRepository patientTypeRepository,
								   PatientExternalMedicalCoverageService patientExternalMedicalCoverageService,
								   FeatureFlagsService featureFlagsService){
		this.hceHealthConditionsExternalService = hceHealthConditionsExternalService;
		this.hceMedicationExternalService = hceMedicationExternalService;
		this.hceAllergyExternalService = hceAllergyExternalService;
		this.hceClinicalObservationExternalService = hceClinicalObservationExternalService;
		this.patientPortalService = patientPortalService;
		this.personExternalService = personExternalService;
		this.patientService = patientService;
		this.additionalDoctorService = additionalDoctorService;
		this.patientTypeRepository = patientTypeRepository;
		this.patientExternalMedicalCoverageService = patientExternalMedicalCoverageService;
		this.featureFlagsService = featureFlagsService;
	}

	@GetMapping("/medications")
	public ResponseEntity<List<HCEMedicationDto>> getMedications() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer patientId = patientPortalService.getPatientId();
		List<HCEMedicationDto> result = hceMedicationExternalService.getMedication(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/allergies")
	public ResponseEntity<List<HCEAllergyDto>> getAllergies() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer patientId = patientPortalService.getPatientId();
		List<HCEAllergyDto> result = hceAllergyExternalService.getAllergies(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/last-2-anthropometric-data")
	public ResponseEntity<List<HCEAnthropometricDataDto>> getLast2AnthropometricData() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer patientId = patientPortalService.getPatientId();
		List<HCEAnthropometricDataDto> result = hceClinicalObservationExternalService.getLast2AnthropometricDataGeneralState(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/riskFactors")
	public ResponseEntity<HCELast2RiskFactorsDto> getRiskFactors() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer patientId = patientPortalService.getPatientId();
		HCELast2RiskFactorsDto result = hceClinicalObservationExternalService.getLast2RiskFactorsGeneralState(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/familyHistories")
	public ResponseEntity<List<HCEPersonalHistoryDto>> getFamilyHistories() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer patientId = patientPortalService.getPatientId();
		List<HCEPersonalHistoryDto> result = hceHealthConditionsExternalService.getFamilyHistories(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/personalHistories")
	public ResponseEntity<List<HCEPersonalHistoryDto>> getPersonalHistories() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer patientId = patientPortalService.getPatientId();
		List<HCEPersonalHistoryDto> result = hceHealthConditionsExternalService.getActivePersonalHistories(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/personalInformation")
	public ResponseEntity<PersonalInformationDto> getPersonalInformation() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer personId = patientPortalService.getPersonId();
		PersonalInformationDto result = personExternalService.getPersonalInformation(personId);
		LOG.debug(LOGGING_OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}

	@GetMapping("/basicdata")
	public ResponseEntity<BasicPatientDto> getBasicDataPatient() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer patientId = patientPortalService.getPatientId();
		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
		BasicPatientDto result = new BasicPatientDto(patient.getId(), personData, patient.getTypeId());
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/photo")
	public ResponseEntity<PersonPhotoDto> getPatientPhoto() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer patientId = patientPortalService.getPatientId();
		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		PersonPhotoDto personPhotoDto = personExternalService.getPersonPhoto(patient.getPersonId());
		LOG.debug(LOGGING_OUTPUT, personPhotoDto);
		return ResponseEntity.ok().body(personPhotoDto);
	}

	@GetMapping("/completedata")
	public ResponseEntity<CompletePatientDto> getCompleteDataPatient() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer patientId = patientPortalService.getPatientId();
		Patient patient = patientService.getPatient(patientId)
				.orElseThrow(() -> new EntityNotFoundException(PATIENT_INVALID));
		DoctorsBo doctorsBo = additionalDoctorService.getAdditionalDoctors(patientId);
		BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
		PatientType patientType = patientTypeRepository.findById(patient.getTypeId()).orElseGet(PatientType::new);
		CompletePatientDto result = new CompletePatientDto(patient, patientType, personData,
				doctorsBo.getGeneralPractitionerBo() != null
						? new AAdditionalDoctorDto(doctorsBo.getGeneralPractitionerBo())
						: null,
				doctorsBo.getPamiDoctorBo() != null ? new AAdditionalDoctorDto(doctorsBo.getPamiDoctorBo()) : null);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping(value = "/coverages")
	public ResponseEntity<List<PatientMedicalCoverageDto>> getActivePatientMedicalCoverages() throws MethodNotSupportedException {
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE))
			throw new MethodNotSupportedException("Funcionalidad no soportada");
		Integer patientId = patientPortalService.getPatientId();
		List<PatientMedicalCoverageDto> result = patientExternalMedicalCoverageService.getActivePrivateMedicalCoverages(patientId);
		LOG.debug(LOGGING_OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}
}
