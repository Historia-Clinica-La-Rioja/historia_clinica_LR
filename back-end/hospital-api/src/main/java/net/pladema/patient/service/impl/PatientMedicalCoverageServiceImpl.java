package net.pladema.patient.service.impl;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.patient.repository.PatientMedicalCoverageRepository;
import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;
import net.pladema.patient.repository.entity.MedicalCoverage;
import net.pladema.patient.repository.entity.PatientMedicalCoverageAssn;
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
import java.util.stream.Collectors;

@Service
public class PatientMedicalCoverageServiceImpl implements PatientMedicalCoverageService {

	private static final Logger LOG = LoggerFactory.getLogger(PatientMedicalCoverageServiceImpl.class);

	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT_DATA = "Input data -> {}";

	private final PatientMedicalCoverageRepository patientMedicalCoverageRepository;

	private final MedicalCoverageRepository medicalCoverageRepository;

	private final MedicalCoveragePlanRepository medicalCoveragePlanRepository;

	public PatientMedicalCoverageServiceImpl(PatientMedicalCoverageRepository patientMedicalCoverageRepository, MedicalCoverageRepository medicalCoverageRepository, MedicalCoveragePlanRepository medicalCoveragePlanRepository) {
		this.patientMedicalCoverageRepository = patientMedicalCoverageRepository;
		this.medicalCoverageRepository = medicalCoverageRepository;
		this.medicalCoveragePlanRepository = medicalCoveragePlanRepository;
	}

	@Override
	public List<PatientMedicalCoverageBo> getActiveCoverages(Integer patientId) {
		LOG.debug(INPUT_DATA, patientId);
		List<PatientMedicalCoverageVo> queryResult = patientMedicalCoverageRepository.getActivePatientCoverages(patientId);
		List<PatientMedicalCoverageBo> result = queryResult.stream().map(PatientMedicalCoverageBo::new).collect(Collectors.toList());
		result.forEach(mc ->{
			if(mc.getPlanId()!=null)
				this.medicalCoveragePlanRepository.findById(mc.getPlanId())
					.ifPresent(plan -> mc.setPlanName(plan.getPlan()));
		});
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<PatientMedicalCoverageBo> getCoverage(Integer patientMedicalCoverageId) {
		LOG.debug(INPUT_DATA, patientMedicalCoverageId);
		Optional<PatientMedicalCoverageBo> result = patientMedicalCoverageRepository.getPatientCoverage(patientMedicalCoverageId)
				.map(PatientMedicalCoverageBo::new).map(bo -> {
					if (bo.getPlanId() != null)
						bo.setPlanName(medicalCoveragePlanRepository.findById(bo.getPlanId()).get().getPlan());
					return bo;
				});
		LOG.debug(OUTPUT, result);
		return result;
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
			this.medicalCoveragePlanRepository.findById(mc.getPlanId())
					.ifPresent(plan -> mc.setPlanName(plan.getPlan()));
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
			coverage.getMedicalCoverage().setId(coverage.getMedicalCoverage().getId());
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
			coverage.getMedicalCoverage().setId(medicalCoverageSaved.getId());
			Integer pmcId = patientMedicalCoverageRepository.getByPatientAndMedicalCoverage(patientId, coverage.getMedicalCoverage().getId())
					.map(PatientMedicalCoverageAssn::getId)
					.orElseGet(() -> patientMedicalCoverageRepository.save(new PatientMedicalCoverageAssn(coverage, patientId)).getId());
			result.add(pmcId);
		});
		return result;
	}
}
