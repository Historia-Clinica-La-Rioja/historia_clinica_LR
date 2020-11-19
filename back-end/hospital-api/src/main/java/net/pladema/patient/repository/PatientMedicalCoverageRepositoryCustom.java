package net.pladema.patient.repository;

import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;

import java.util.List;
import java.util.Optional;

public interface PatientMedicalCoverageRepositoryCustom {

	List<PatientMedicalCoverageVo> getActivePatientCoverages(Integer patientId);

	List<PatientMedicalCoverageVo> getActivePatientHealthInsurances(Integer patientId);

	List<PatientMedicalCoverageVo> getActivePatientPrivateHealthInsurances(Integer patientId);
}
