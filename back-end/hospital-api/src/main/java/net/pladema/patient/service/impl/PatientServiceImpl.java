package net.pladema.patient.service.impl;

import net.pladema.federar.services.FederarService;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.PatientMedicalCoverageRepository;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.PrivateHealthInsuranceDetailsRepository;
import net.pladema.patient.repository.domain.PatientPersonVo;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.LimitedPatientSearchBo;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.MedicalCoverageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static net.pladema.patient.service.MathScore.calculateMatch;

@Service
public class PatientServiceImpl implements PatientService {


	private static final Logger LOG = LoggerFactory.getLogger(PatientServiceImpl.class);

	private static final float THRESHOLD = 60.0f;
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT_DATA = "Input data -> {}";

	public static final Integer MAX_RESULT_SIZE = 150;

	private final PatientRepository patientRepository;

	public PatientServiceImpl(PatientRepository patientRepository,
							  PatientMedicalCoverageRepository patientMedicalCoverageRepository,
							  MedicalCoverageRepository medicalCoverageRepository,
							  PrivateHealthInsuranceDetailsRepository privateHealthInsuranceDetailsRepository,
							  FederarService federarService) {
		this.patientRepository = patientRepository;
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
		Integer actualPatientListSize = patientRepository.getCountByOptionalFilter(searchFilter).intValue();
		List<PatientSearch> patientList = patientRepository.getAllByOptionalFilter(searchFilter, MAX_RESULT_SIZE);
		LimitedPatientSearchBo result = new LimitedPatientSearchBo(patientList, actualPatientListSize);
		LOG.trace(OUTPUT, result);
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
		Patient patientSaved = patientRepository.save(patientToSave);
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
		patientRepository.save(patient);
	}

}
