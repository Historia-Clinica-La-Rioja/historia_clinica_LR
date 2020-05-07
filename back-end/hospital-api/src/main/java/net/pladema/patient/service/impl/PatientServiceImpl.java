package net.pladema.patient.service.impl;

import static net.pladema.patient.service.MathScore.calculateMatch;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.LocalIdSearchResponse;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.entity.Person;

@Service
public class PatientServiceImpl implements PatientService {

	private static final Logger LOG = LoggerFactory.getLogger(PatientServiceImpl.class);

	private static final float THRESHOLD = 75.0f;
	private final PatientRepository patientRepository;

	private final FederarService federarService;

	public PatientServiceImpl(PatientRepository patientRepository, FederarService federarService) {
		this.patientRepository = patientRepository;
		this.federarService = federarService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientSearch> searchPatient(PatientSearchFilter searchFilter) {
		Stream<PatientSearch> allPatients = patientRepository.getAllByFilter(searchFilter.getFirstName(),
				searchFilter.getLastName(), searchFilter.getIdentificationNumber(), searchFilter.getBirthDate());
		return allPatients.peek(patient -> patient.setRanking(calculateMatch(searchFilter, patient.getPerson())))
				.filter(patient -> patient.getRanking() > THRESHOLD).collect(Collectors.toList());
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
		Optional<LocalIdSearchResponse> federarReponse = federarService.federatePatient(person, patient);
		federarReponse.ifPresent(updatePatientPermanent(patient));
	}

	private Consumer<LocalIdSearchResponse> updatePatientPermanent(Patient patient) {
		return federatedPatient -> federatedPatient.getNationalId().ifPresent(nationalId -> {
			patient.setNationalId(nationalId);
			patient.setTypeId(PatientType.PERMANENT);
			patientRepository.save(patient);
			LOG.debug("Succesfuly federated patient with nationalId => {}", nationalId);
		});
	}

}
