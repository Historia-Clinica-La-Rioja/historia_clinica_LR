package ar.lamansys.sgh.shared.infrastructure.input.service.medicalcoverage;

import ar.lamansys.sgh.shared.domain.medicalcoverage.SharedPatientMedicalCoverageBo;
import ar.lamansys.sgh.shared.infrastructure.output.repository.SharedPatientMedicalCoverageRepository;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PatientMedicalCoverageService {

	private static final Logger LOG = LoggerFactory.getLogger(PatientMedicalCoverageService.class);

	private final SharedPatientMedicalCoverageRepository patientMedicalCoverageRepository;

	public Optional<SharedPatientMedicalCoverageBo> getCoverage(Integer patientMedicalCoverageId) {
		LOG.debug("Input parameters: patientMedicalCoverageId {}", patientMedicalCoverageId);
		Optional<SharedPatientMedicalCoverageBo> result = patientMedicalCoverageRepository.getPatientMedicalCoverageById(patientMedicalCoverageId);
		LOG.debug("Result: {}", result);
		return result;
	}


}
