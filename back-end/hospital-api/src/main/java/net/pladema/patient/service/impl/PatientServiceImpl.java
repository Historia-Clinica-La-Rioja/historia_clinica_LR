package net.pladema.patient.service.impl;

import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.LocalIdSearchResponse;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.PatientMedicalCoverageRepository;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.PrivateHealthInsuranceDetailsRepository;
import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;
import net.pladema.patient.repository.entity.*;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.MedicalCoverageBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.MedicalCoverageRepository;
import net.pladema.person.repository.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static net.pladema.patient.service.MathScore.calculateMatch;

@Service
public class PatientServiceImpl implements PatientService {

	private static final Logger LOG = LoggerFactory.getLogger(PatientServiceImpl.class);

	private static final float THRESHOLD = 60.0f;

	private final PatientRepository patientRepository;

	private final PatientMedicalCoverageRepository patientMedicalCoverageRepository;

	private final MedicalCoverageRepository medicalCoverageRepository;

	private final PrivateHealthInsuranceDetailsRepository privateHealthInsuranceDetailsRepository;

	private final FederarService federarService;

	public PatientServiceImpl(PatientRepository patientRepository,
							  PatientMedicalCoverageRepository patientMedicalCoverageRepository,
							  MedicalCoverageRepository medicalCoverageRepository,
							  PrivateHealthInsuranceDetailsRepository privateHealthInsuranceDetailsRepository, FederarService federarService) {
		this.patientRepository = patientRepository;
		this.patientMedicalCoverageRepository = patientMedicalCoverageRepository;
		this.medicalCoverageRepository = medicalCoverageRepository;
		this.privateHealthInsuranceDetailsRepository = privateHealthInsuranceDetailsRepository;
		this.federarService = federarService;
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

	@Override
	public List<PatientMedicalCoverageBo> getCoverages(Integer patientId) {
		LOG.debug("Input data -> {}", patientId);
		List<PatientMedicalCoverageVo> queryResult = patientRepository.getPatientCoverages(patientId);
		List<PatientMedicalCoverageBo> result = queryResult.stream().map(PatientMedicalCoverageBo::new).collect(Collectors.toList());
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	public PatientMedicalCoverageBo getCoverage(Integer patientMedicalCoverageId) {
		LOG.debug("Input data -> {}", patientMedicalCoverageId);
		PatientMedicalCoverageVo queryResult = patientRepository.getPatientCoverage(patientMedicalCoverageId);
		PatientMedicalCoverageBo result = new PatientMedicalCoverageBo(queryResult);
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<PatientMedicalCoverageBo> getHealthInsurances(Integer patientId) {
		LOG.debug("Input data -> {}", patientId);
		List<PatientMedicalCoverageVo> queryResult = patientRepository.getPatientHealthInsurances(patientId);
		List<PatientMedicalCoverageBo> result = queryResult.stream().map(PatientMedicalCoverageBo::new).collect(Collectors.toList());
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<PatientMedicalCoverageBo> getPrivateHealthInsurances(Integer patientId) {
		LOG.debug("Input data -> {}", patientId);
		List<PatientMedicalCoverageVo> queryResult = patientRepository.getPatientPrivateHealthInsurances(patientId);
		List<PatientMedicalCoverageBo> result = queryResult.stream().map(PatientMedicalCoverageBo::new).collect(Collectors.toList());
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	@Transactional
	public List<Integer> saveCoverages(List<PatientMedicalCoverageBo> coverages,
									   Integer patientId){
		List<Integer> result = new ArrayList<>();
		coverages.forEach((coverage)->{
			MedicalCoverageBo medicalCoverage = coverage.getMedicalCoverage();
			MedicalCoverage MedicalCoverageSaved = medicalCoverageRepository.save(medicalCoverage.mapToEntity());
			coverage.getMedicalCoverage().setId(MedicalCoverageSaved.getId());
			if (coverage.getPrivateHealthInsuranceDetails() != null){
				PrivateHealthInsuranceDetails phidSaved = privateHealthInsuranceDetailsRepository.save(new PrivateHealthInsuranceDetails(coverage.getPrivateHealthInsuranceDetails()));
				coverage.getPrivateHealthInsuranceDetails().setId(phidSaved.getId());
			}
			PatientMedicalCoverageAssn pmcToSave = new PatientMedicalCoverageAssn(coverage, patientId);
			PatientMedicalCoverageAssn pmcSaved= patientMedicalCoverageRepository.save(pmcToSave);
			result.add(pmcSaved.getId());
		});
		return result;
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
