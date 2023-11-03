package net.pladema.patient.repository;

import java.util.List;
import java.util.Optional;

import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;

public interface PatientMedicalCoverageRepositoryCustom {

	List<PatientMedicalCoverageVo> getActivePatientCoverages(Integer patientId);

	Optional<PatientMedicalCoverageVo> getActivePatientCoverageByOrderId(Integer orderId);

	List<PatientMedicalCoverageVo> getActivePatientHealthInsurances(Integer patientId);

	List<PatientMedicalCoverageVo> getActivePatientPrivateHealthInsurances(Integer patientId);
}
