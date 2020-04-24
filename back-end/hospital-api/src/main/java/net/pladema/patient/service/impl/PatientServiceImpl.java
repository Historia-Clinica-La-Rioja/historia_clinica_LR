package net.pladema.patient.service.impl;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.domain.BasicListedPatient;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.pladema.patient.service.MathScore.calculateMatch;

@Service
public class PatientServiceImpl implements PatientService {

	private static final Logger LOG = LoggerFactory.getLogger(PatientServiceImpl.class);

	private static final float THRESHOLD = 75.0f;
	private final PatientRepository patientRepository;

	public PatientServiceImpl(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientSearch> searchPatient(PatientSearchFilter searchFilter) {
		Stream<PatientSearch> allPatients = patientRepository.getAllByFilter(searchFilter.getFirstName(),
				searchFilter.getLastName(), searchFilter.getIdentificationNumber(), searchFilter.getBirthDate());
		return allPatients
				.peek(patient -> patient.setRanking(calculateMatch(searchFilter, patient.getPerson())))
				.filter(patient -> patient.getRanking() > THRESHOLD)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Patient> getPatient(Integer patientId) {
		LOG.debug("Input data -> {}", patientId);
		Optional<Patient> result = patientRepository.findById(patientId);
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Patient addPatient(Patient patientToSave) {
		LOG.debug("Going to save -> {}", patientToSave);
		Patient patientSaved = patientRepository.save(patientToSave);
		LOG.debug("Saved -> {}", patientSaved);
		return patientSaved;
	}

	public List<BasicListedPatient> getPatients() {
		List<BasicListedPatient> result = patientRepository.findAllPatientsListedData();
		LOG.debug("Output -> {}", result);
		return result;
	}
}
