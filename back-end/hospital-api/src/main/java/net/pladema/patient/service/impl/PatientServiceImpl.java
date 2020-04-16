package net.pladema.patient.service.impl;

import static net.pladema.patient.service.MathScore.calculateMatch;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.entity.Person;

@Service
public class PatientServiceImpl implements PatientService {

	private static final float THRESHOLD = 75.0f;
	private final PatientRepository patientRepository;

	public PatientServiceImpl(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientSearch> searchPatient(PatientSearchFilter searchFilter) {
		Stream<Person> allPatients = patientRepository.getAllByFilter(searchFilter.getFirstName(),
				searchFilter.getLastName(), searchFilter.getIdentificationNumber(), searchFilter.getBirthDate());
		return allPatients
				.map(patient -> new PatientSearch(patient, calculateMatch(searchFilter, patient)))
				.filter(patientDto -> patientDto.getRanking() > THRESHOLD)
				.collect(Collectors.toList());
	}

}
