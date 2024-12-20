package net.pladema.patient.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.patient.application.port.output.PatientMedicalCoveragePort;

import net.pladema.patient.domain.GetMedicalCoverageHealthInsuranceValidationDataBo;

import net.pladema.patient.repository.PatientMedicalCoverageRepository;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PatientMedicalCoveragePortImpl implements PatientMedicalCoveragePort {

	private final PatientMedicalCoverageRepository patientMedicalCoverageRepository;

	@Override
	public GetMedicalCoverageHealthInsuranceValidationDataBo getMedicalCoverageHealthInsuranceValidationDataById(Integer medicalCoverageId) {
		return patientMedicalCoverageRepository.fetchMedicalCoverageHealthInsuranceValidationDataById(medicalCoverageId);
	}

}
