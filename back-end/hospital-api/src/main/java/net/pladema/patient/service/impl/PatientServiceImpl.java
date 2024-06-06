package net.pladema.patient.service.impl;

import static net.pladema.patient.service.MathScore.calculateMatch;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.patient.repository.entity.PatientHistory;

import net.pladema.patient.service.domain.PatientGenderAgeBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EAuditType;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;
import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.audit.repository.HospitalAuditRepository;
import net.pladema.audit.repository.entity.HospitalAudit;
import net.pladema.audit.service.domain.enums.EActionType;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.federar.services.FederarService;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.patient.controller.dto.AuditablePatientInfoDto;
import net.pladema.patient.controller.dto.MergedPatientSearchFilter;
import net.pladema.patient.controller.dto.PatientRegistrationSearchFilter;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.controller.service.exception.RejectedPatientException;
import net.pladema.patient.controller.service.exception.RejectedPatientExceptionEnum;
import net.pladema.patient.repository.AuditablePatientRepository;
import net.pladema.patient.repository.MedicalCoverageRepository;
import net.pladema.patient.repository.MergedInactivePatientRepository;
import net.pladema.patient.repository.MergedPatientRepository;
import net.pladema.patient.repository.PatientAuditRepository;
import net.pladema.patient.repository.PatientHistoryRepository;
import net.pladema.patient.repository.PatientMedicalCoverageRepository;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.PatientRepositoryImpl;
import net.pladema.patient.repository.PatientTypeRepository;
import net.pladema.patient.repository.PrivateHealthInsuranceDetailsRepository;
import net.pladema.patient.repository.domain.PatientPersonVo;
import net.pladema.patient.repository.entity.AuditablePatient;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientAudit;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.AuditablePatientInfoBo;
import net.pladema.patient.service.domain.LimitedPatientSearchBo;
import net.pladema.patient.service.domain.MergedPatientSearch;
import net.pladema.patient.service.domain.PatientRegistrationSearch;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.domain.PersonSearchResultVo;


@Service
public class PatientServiceImpl implements PatientService {


	private static final Logger LOG = LoggerFactory.getLogger(PatientServiceImpl.class);

	private static final float THRESHOLD = 60.0f;
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT_DATA = "Input data -> {}";

	public static final Integer MAX_RESULT_SIZE = 150;

	private final PatientRepository patientRepository;
	private final HospitalAuditRepository hospitalAuditRepository;
	private final PatientAuditRepository patientAuditRepository;
	private final AuditablePatientRepository auditablePatientRepository;
	private final FeatureFlagsService featureFlagsService;
	private final LocalDateMapper localDateMapper;
	private final PatientTypeRepository patientTypeRepository;
	private final MergedPatientRepository mergedPatientRepository;

	private final InternmentEpisodeRepository internmentEpisodeRepository;
	private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;
	private final AppointmentRepository appointmentRepository;
	private final PatientHistoryRepository patientHistoryRepository;
	private final MergedInactivePatientRepository mergedInactivePatientRepository;
	private final PatientRepositoryImpl patientRepositoryCustom;

	public PatientServiceImpl(PatientRepository patientRepository,
							  PatientMedicalCoverageRepository patientMedicalCoverageRepository,
							  MedicalCoverageRepository medicalCoverageRepository,
							  PrivateHealthInsuranceDetailsRepository privateHealthInsuranceDetailsRepository,
							  FederarService federarService,
							  HospitalAuditRepository hospitalAuditRepository,
							  PatientAuditRepository patientAuditRepository,
							  FeatureFlagsService featureFlagsService,
							  AuditablePatientRepository auditablePatientRepository,
							  LocalDateMapper localDateMapper, PatientTypeRepository patientTypeRepository,
							  InternmentEpisodeRepository internmentEpisodeRepository,
							  EmergencyCareEpisodeRepository emergencyCareEpisodeRepository,
							  AppointmentRepository appointmentRepository,
							  PatientHistoryRepository patientHistoryRepository,
							  MergedPatientRepository mergedPatientRepository,
							  MergedInactivePatientRepository mergedInactivePatientRepository,
							  PatientRepositoryImpl patientRepositoryCustom) {
		this.patientRepository = patientRepository;
		this.hospitalAuditRepository = hospitalAuditRepository;
		this.patientAuditRepository = patientAuditRepository;
		this.featureFlagsService = featureFlagsService;
		this.auditablePatientRepository = auditablePatientRepository;
		this.localDateMapper = localDateMapper;
		this.patientTypeRepository = patientTypeRepository;
		this.internmentEpisodeRepository = internmentEpisodeRepository;
		this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
		this.appointmentRepository = appointmentRepository;
		this.patientHistoryRepository = patientHistoryRepository;
		this.mergedPatientRepository = mergedPatientRepository;
		this.mergedInactivePatientRepository = mergedInactivePatientRepository;
		this.patientRepositoryCustom = patientRepositoryCustom;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientSearch> searchPatient(PatientSearchFilter searchFilter) {
		LOG.debug("Input parameter -> searchFilter {}", searchFilter);
		List<PatientSearch> allPatients = patientRepository.getAllByFilter(searchFilter);
		List<PatientSearch> matchedPatients = allPatients.stream()
				.peek(patient -> patient.setRanking(calculateMatch(searchFilter, patient.getPerson())))
				.filter(patient -> patient.getRanking() > THRESHOLD)
				.sorted(Comparator.comparing(PatientSearch::getRanking).reversed())
				.collect(Collectors.toList());
		LOG.debug(OUTPUT, matchedPatients);
		return matchedPatients;
	}

	@Override
	@Transactional(readOnly = true)
	public LimitedPatientSearchBo searchPatientOptionalFilters(PatientSearchFilter searchFilter) {
		LOG.debug(INPUT_DATA, searchFilter);
		boolean filterByNameSelfDetermination = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);
		searchFilter.setFilterByNameSelfDetermination(filterByNameSelfDetermination);
		Integer actualPatientListSize = patientRepository.getCountByOptionalFilter(searchFilter);
		List<PatientSearch> patientList = patientRepository.getAllByOptionalFilter(searchFilter, MAX_RESULT_SIZE);
		LimitedPatientSearchBo result = new LimitedPatientSearchBo(patientList, actualPatientListSize);
		LOG.trace(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<Patient> getActivePatient(Integer patientId) {
		LOG.debug(INPUT_DATA, patientId);
		Optional<Patient> result = patientRepository.findActivePatientById(patientId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<Patient> getPatient(Integer patientId) {
		LOG.debug(INPUT_DATA, patientId);
		Optional<Patient> result = patientRepository.findById(patientId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<Patient> getPatients(Set<Integer> ids) {
		LOG.debug(INPUT_DATA, ids.size());
		LOG.trace(INPUT_DATA, ids);
		List<Patient> result = patientRepository.findAllById(ids);
		LOG.debug("Result size {}", result.size());
		LOG.trace(OUTPUT, result);
		return result;
	}

	@Override
	public Patient addPatient(Patient patientToSave) {
		LOG.debug("Going to save -> {}", patientToSave);
		Short auditTypeId = patientToSave.getAuditTypeId();
		if (auditTypeId == null){
			patientToSave.setAuditTypeId(EAuditType.UNAUDITED.getId());
		}
		boolean shouldPersistPatientHistory = hasDifferentPatientData(patientToSave);
		Patient patientSaved = patientRepository.save(patientToSave);
		if(shouldPersistPatientHistory)
			patientHistoryRepository.save(new PatientHistory(patientSaved));
		LOG.debug("Saved -> {}", patientSaved);
		return patientSaved;
	}

	@Override
	public List<PatientPersonVo> getAllValidatedPatients() {
		LOG.debug("Getting all validated patients");
		return patientRepository.getAllByPatientType(PatientType.VALIDATED);
	}

	@Override
	public void updatePatientPermanent(PatientPersonVo patientPersonVo, Integer nationalId) {
		LOG.debug("Updating to permanent patient -> patientPersonVo {}, nationalId {}", patientPersonVo, nationalId);
		Patient patient = new Patient(patientPersonVo);
		patient.setNationalId(nationalId);
		patient.setTypeId(PatientType.PERMANENT);
		addPatient(patient);
		this.auditActionPatient(null, patient.getId(), EActionType.UPDATE);
	}

	@Override
	public void auditActionPatient(Integer institutionId, Integer patientId, EActionType eActionType) {
		LOG.debug("Add audit -> institutionId {}, patientId {}, eActionType {}", institutionId, patientId, eActionType);
		Integer userId = SecurityContextUtils.getUserDetails().userId;
		LocalDateTime dateNow = LocalDateTime.now();

		HospitalAudit hospitalAuditToSave = new HospitalAudit();
		hospitalAuditToSave.setInstitutionId(institutionId);
		hospitalAuditToSave.setUserId(userId);
		hospitalAuditToSave.setDate(dateNow);
		hospitalAuditToSave.setActionType(eActionType.getId());

		HospitalAudit hospitalAuditSaved = hospitalAuditRepository.save(hospitalAuditToSave);
		LOG.debug("Saved -> {}", hospitalAuditSaved);
		PatientAudit patientAuditSaved = patientAuditRepository.save(new PatientAudit(patientId, hospitalAuditSaved.getId()));
		LOG.debug("Saved -> {}", patientAuditSaved);
	}

	@Override
	public Optional<String> getIdentificationNumber(Integer patientId) {
		LOG.debug("Input parameter -> patientId {}", patientId);
		Optional<String> result = patientRepository.getIdentificationNumber(patientId);
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	public void persistSelectionForAnAudict(Integer patientId, Integer institutionId, String message) {
		LOG.debug("Input parameter -> patientId {}, institutionId {}, message {}", patientId, institutionId, message);
		Integer userId = SecurityContextUtils.getUserDetails().userId;
		AuditablePatient auditablePatient = new AuditablePatient();
				auditablePatient.setPatientId(patientId);
				auditablePatient.setInstitutionId(institutionId);
				auditablePatient.setCreatedBy(userId);
				auditablePatient.setMessage(message);
		auditablePatientRepository.save(auditablePatient);
	}

	@Override
	@Transactional(readOnly = true)
	public AuditablePatientInfoDto getAuditablePatientInfo(Integer patientId) {
		AuditablePatientInfoBo auditablePatientInfo = auditablePatientRepository.getLastSelectionForAnAudict(patientId)
				.findFirst().orElse(null);
		if (auditablePatientInfo != null) {
			auditablePatientInfo.setIncludeNameSelfDetermination(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
			AuditablePatientInfoDto result = mapToAuditablePatientInfoDto(auditablePatientInfo);
			LOG.debug("Output -> {} ", result);
			return result;
		}
		LOG.debug("Output -> No existen mensajes de auditoria para el paciente con id {} ", patientId);
		return null;
	}

	@Override
	public List<PatientRegistrationSearch> getPatientsRegistrationByFilter(PatientRegistrationSearchFilter searchFilter) {
		LOG.debug("Input parameter -> searchFilter {}", searchFilter);
		List<PatientRegistrationSearch> allPatients = patientRepository.getAllRegistrationByFilter(searchFilter);
		List<PatientRegistrationSearch> matchedPatients = allPatients.stream()
				.peek(patient -> patient.setRanking(calculateMatch(searchFilter, patient.getPerson())))
				.filter(patient -> patient.getRanking() > THRESHOLD)
				.sorted(Comparator.comparing(PatientRegistrationSearch::getRanking).reversed())
				.collect(Collectors.toList());
		LOG.debug(OUTPUT, matchedPatients);
		return matchedPatients;
	}

	@Override
	public List<PatientRegistrationSearch> getPatientRegistrationById(Integer patientId) {
		LOG.debug("Input parameter -> patientId {}", patientId);
		Optional<PatientRegistrationSearch> patientRegistration = patientRepository.getPatientRegistrationSearchById(patientId, EPatientType.getAllTypeIdsForAudit());
		if (patientRegistration.isPresent()) {
			PatientRegistrationSearch result = patientRegistration.get();
			result.setRanking(100.0f);
			LOG.debug(OUTPUT, result);
			return List.of(result);
		}
		LOG.debug("Output -> No existe paciente con el id {} ", patientId);
		return Collections.emptyList();
	}

	@Override
	public List<PatientType> getPatientTypesForAuditor() {
		List<Short> patientTypesId = EPatientType.getAllTypeIdsForAudit();
		List<PatientType> result = patientTypeRepository.findAll()
				.stream().filter(i -> patientTypesId.contains(i.getId())).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}
	
	@Override
	public List<MergedPatientSearch> getMergedPatientsByFilter(MergedPatientSearchFilter searchFilter) {
		LOG.debug("Input parameter -> searchFilter {}", searchFilter);
		List<MergedPatientSearch> result = mergedPatientRepository.getAllByFilter(searchFilter);
		LOG.debug(OUTPUT,  result);
		return  result;
	}

	@Override
	public List<PersonSearchResultVo> getMergedPersonsByPatientId(Integer activePatientId) {
		LOG.debug("Input parameter -> activePatientId {}", activePatientId);
		List<PersonSearchResultVo> result = mergedInactivePatientRepository.findMergedPersonInfoByActivePatientId(activePatientId);
		LOG.debug(OUTPUT,  result);
		return  result;
	}

	@Override
	public List<PatientRegistrationSearch> getPatientsToAudit() {
		PatientRegistrationSearchFilter filter = new PatientRegistrationSearchFilter();
		filter.setToAudit(true);
		List<PatientRegistrationSearch> allPatientsToAudit = patientRepository.getAllRegistrationByFilter(filter);
		LOG.debug(OUTPUT, allPatientsToAudit);
		return allPatientsToAudit;
	}

	@Override
	public void assertHasActiveEncountersByPatientId(Integer patientId) {
		if(internmentEpisodeRepository.isPatientHospitalized(patientId) || emergencyCareEpisodeRepository.existsActiveEpisodeByPatientId(patientId) || appointmentRepository.existsAppointmentByStatesAndPatientId(List.of(AppointmentState.ASSIGNED, AppointmentState.CONFIRMED), patientId))
			throw new RejectedPatientException(RejectedPatientExceptionEnum.ENCOUNTER_ACTIVE_EXISTS, "El paciente posee un encuentro activo");
	}
	
	private boolean hasDifferentPatientData(Patient newData) {
		return Optional.ofNullable(newData.getId()).map(id ->
			patientRepository.findById(id).map(old -> !(old.getTypeId().equals(newData.getTypeId()) && old.getAuditTypeId().equals(newData.getAuditTypeId()) && Objects.equals(old.getNationalId(),newData.getNationalId())))
					.orElse(true)
		).orElse(true);
	}
	
	@Override
	public List<Patient> getLongTermTemporaryPatientIds(LocalDateTime maxDate, Short limit) {
		LOG.debug("Input parameters -> maxDate {}, limit {}", maxDate, limit);
		List<Patient> result = patientRepositoryCustom.getLongTermTemporaryPatientIds(maxDate, limit);
		LOG.debug("Output result -> {}", result);
		return result;
	}

	@Override
	public Optional<PatientGenderAgeBo> getPatientGenderAge(Integer patientId){
		LOG.debug("Input parameters -> patientId {}", patientId);
		Optional<PatientGenderAgeBo> result = patientRepository.getPatientGenderAge(patientId);
		LOG.debug("Output -> result {}", result);
		return result;
	}

	private AuditablePatientInfoDto mapToAuditablePatientInfoDto(AuditablePatientInfoBo auditablePatientInfo) {
		return AuditablePatientInfoDto.builder()
				.message(auditablePatientInfo.getMessage())
				.createdBy(auditablePatientInfo.getAuthorFullName())
				.createdOn(localDateMapper.toDateTimeDto(auditablePatientInfo.getCreatedOn()))
				.institutionName(auditablePatientInfo.getInstitutionName())
				.build();
	}

}