package net.pladema.patient.service.impl;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.establishment.repository.PrivateHealthInsurancePlanRepository;
import net.pladema.patient.repository.PatientMedicalCoverageRepository;
import net.pladema.patient.repository.PrivateHealthInsuranceDetailsRepository;
import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;
import net.pladema.patient.repository.entity.MedicalCoverage;
import net.pladema.patient.repository.entity.PatientMedicalCoverageAssn;
import net.pladema.patient.repository.entity.PrivateHealthInsuranceDetails;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.patient.repository.MedicalCoverageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class PatientMedicalCoverageServiceImpl implements PatientMedicalCoverageService {

	private static final Logger LOG = LoggerFactory.getLogger(PatientMedicalCoverageServiceImpl.class);

	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT_DATA = "Input data -> {}";

	private final PatientMedicalCoverageRepository patientMedicalCoverageRepository;

	private final MedicalCoverageRepository medicalCoverageRepository;

	private final PrivateHealthInsuranceDetailsRepository privateHealthInsuranceDetailsRepository;

	private final PrivateHealthInsurancePlanRepository privateHealthInsurancePlanRepository;

	public PatientMedicalCoverageServiceImpl(PatientMedicalCoverageRepository patientMedicalCoverageRepository, MedicalCoverageRepository medicalCoverageRepository, PrivateHealthInsuranceDetailsRepository privateHealthInsuranceDetailsRepository, PrivateHealthInsurancePlanRepository privateHealthInsurancePlanRepository) {
		this.patientMedicalCoverageRepository = patientMedicalCoverageRepository;
		this.medicalCoverageRepository = medicalCoverageRepository;
		this.privateHealthInsuranceDetailsRepository = privateHealthInsuranceDetailsRepository;
		this.privateHealthInsurancePlanRepository = privateHealthInsurancePlanRepository;
	}

	@Override
	public List<PatientMedicalCoverageBo> getActiveCoverages(Integer patientId) {
		LOG.debug(INPUT_DATA, patientId);
		List<PatientMedicalCoverageVo> queryResult = patientMedicalCoverageRepository.getActivePatientCoverages(patientId);
		List<PatientMedicalCoverageBo> result = queryResult.stream().map(PatientMedicalCoverageBo::new).collect(Collectors.toList());
		result.forEach(mc ->{
			if(mc.getPrivateHealthInsuranceDetails().getPlanId()!=null)
				this.privateHealthInsurancePlanRepository.findById(mc.getPrivateHealthInsuranceDetails().getPlanId())
					.ifPresent(plan -> mc.getPrivateHealthInsuranceDetails().setPlanName(plan.getPlan()));
		});
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<PatientMedicalCoverageBo> getCoverage(Integer patientMedicalCoverageId) {
		LOG.debug(INPUT_DATA, patientMedicalCoverageId);
		Optional<PatientMedicalCoverageVo> queryResult = patientMedicalCoverageRepository.getPatientCoverage(patientMedicalCoverageId);
		AtomicReference<Optional<PatientMedicalCoverageBo>> result = new AtomicReference<>(Optional.empty());
		queryResult.ifPresent(r -> {
			result.set(Optional.of(new PatientMedicalCoverageBo(r)));
		});
		LOG.debug(OUTPUT, result);
		return result.get();
	}

	@Override
	public List<PatientMedicalCoverageBo> getActiveHealthInsurances(Integer patientId) {
		LOG.debug(INPUT_DATA, patientId);
		List<PatientMedicalCoverageVo> queryResult = patientMedicalCoverageRepository.getActivePatientHealthInsurances(patientId);
		List<PatientMedicalCoverageBo> result = queryResult.stream().map(PatientMedicalCoverageBo::new).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<PatientMedicalCoverageBo> getActivePrivateHealthInsurances(Integer patientId) {
		LOG.debug(INPUT_DATA, patientId);
		List<PatientMedicalCoverageVo> queryResult = patientMedicalCoverageRepository.getActivePatientPrivateHealthInsurances(patientId);
		List<PatientMedicalCoverageBo> result = queryResult.stream().map(PatientMedicalCoverageBo::new).collect(Collectors.toList());
		result.forEach(mc ->{
			this.privateHealthInsurancePlanRepository.findById(mc.getPrivateHealthInsuranceDetails().getPlanId())
					.ifPresent(plan -> mc.getPrivateHealthInsuranceDetails().setPlanName(plan.getPlan()));
		});
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	@Transactional
	public List<Integer> saveCoverages(List<PatientMedicalCoverageBo> coverages,
									   Integer patientId) {
		List<Integer> result = new ArrayList<>();
		coverages.forEach((coverage) -> {
			MedicalCoverage medicalCoverage = (coverage.getMedicalCoverage().getId() != null)
					? medicalCoverageRepository.findById(coverage.getMedicalCoverage().getId()).get()
					: coverage.getMedicalCoverage().mapToEntity();
			MedicalCoverage MedicalCoverageSaved = medicalCoverageRepository.save(medicalCoverage);
			coverage.getMedicalCoverage().setId(MedicalCoverageSaved.getId());
			if (coverage.getPrivateHealthInsuranceDetails() != null) {
				PrivateHealthInsuranceDetails phidSaved = privateHealthInsuranceDetailsRepository.save(new PrivateHealthInsuranceDetails(coverage.getPrivateHealthInsuranceDetails()));
				coverage.getPrivateHealthInsuranceDetails().setId(phidSaved.getId());
			}
			PatientMedicalCoverageAssn pmcToSave = new PatientMedicalCoverageAssn(coverage, patientId);
			PatientMedicalCoverageAssn pmcSaved = patientMedicalCoverageRepository.save(pmcToSave);
			result.add(pmcSaved.getId());
		});
		return result;
	}

	@Override
	public List<Integer> saveExternalCoverages(List<PatientMedicalCoverageBo> coverages, Integer patientId) {
		List<Integer> result = new ArrayList<>();
		coverages.forEach((coverage) -> {
			MedicalCoverage medicalCoverageSaved = Optional.ofNullable(coverage.getMedicalCoverage().getId())
					.map(medicalCoverageId -> medicalCoverageRepository.findById(medicalCoverageId)
							.orElse(null))
					.orElseGet(() -> medicalCoverageRepository.findByCUIT(coverage.getMedicalCoverage().getCuit())
							.orElseThrow(() -> new NotFoundException("medical-coverage-not-exists", String.format("La cobertura médica con cuit %s no existe", coverage.getMedicalCoverage().getCuit()))));
			if (coverage.getPrivateHealthInsuranceDetails() != null) {
				PrivateHealthInsuranceDetails phidSaved = privateHealthInsuranceDetailsRepository.save(new PrivateHealthInsuranceDetails(coverage.getPrivateHealthInsuranceDetails()));
				coverage.getPrivateHealthInsuranceDetails().setId(phidSaved.getId());
			}
			coverage.getMedicalCoverage().setId(medicalCoverageSaved.getId());
			Integer pmcId = patientMedicalCoverageRepository.getByPatientAndMedicalCoverage(patientId, coverage.getMedicalCoverage().getId())
					.map(PatientMedicalCoverageAssn::getId)
					.orElseGet(() -> patientMedicalCoverageRepository.save(new PatientMedicalCoverageAssn(coverage, patientId)).getId());
			result.add(pmcId);
		});
		return result;
	}
}
