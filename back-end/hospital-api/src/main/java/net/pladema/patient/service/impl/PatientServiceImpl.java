package net.pladema.patient.service.impl;

import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.LocalIdSearchResponse;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static net.pladema.patient.service.MathScore.*;

@Service
public class PatientServiceImpl implements PatientService {

	private static final Logger LOG = LoggerFactory.getLogger(PatientServiceImpl.class);

	private static final float THRESHOLD = 60.0f;
	private final PatientRepository patientRepository;

	private final FederarService federarService;

	public PatientServiceImpl(PatientRepository patientRepository, FederarService federarService) {
		this.patientRepository = patientRepository;
		this.federarService = federarService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientSearch> searchPatient(PatientSearchFilter searchFilter) {
		LOG.debug("Input parameter -> searchFilter {}", searchFilter);
		List<PatientSearch> allPatients = patientRepository.getAllByFilter(searchFilter);
		float scoreMultiplier = calculateScoreMultiplier(searchFilter);
		List<PatientSearch> matchedPatients = allPatients.stream()
				.peek(patient -> patient.setRanking(calculateMatchAdjustedToMultiplier(searchFilter, patient.getPerson(), scoreMultiplier)))
				.filter(patient -> patient.getRanking() > THRESHOLD)
				.collect(Collectors.toList());
		LOG.debug("Output -> {}", matchedPatients);
		return matchedPatients;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientSearch> searchPatientOptionalFilters(PatientSearchFilter searchFilter) {
		return patientRepository.getAllByOptionalFilter(searchFilter);
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

	@Override
	@Async
	public void federatePatient(Patient patient, Person person) {
		LOG.debug("Going to federate Patient => {} /n with Person => {}", patient, person);
		Optional<LocalIdSearchResponse> federarResponse = federarService.federatePatient(person, patient);
		federarResponse.ifPresent(updatePatientPermanent(patient));
	}

	private Consumer<LocalIdSearchResponse> updatePatientPermanent(Patient patient) {
		return federatedPatient -> federatedPatient.getNationalId().ifPresent(nationalId -> {
			patient.setNationalId(nationalId);
			patient.setTypeId(PatientType.PERMANENT);
			patientRepository.save(patient);
			LOG.debug("Successful federated patient with nationalId => {}", nationalId);
		});
	}

}
