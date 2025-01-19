package net.pladema.patient.infrastructure.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.MedicationRequestValidationDispatcherPatientBo;
import lombok.RequiredArgsConstructor;
import net.pladema.patient.application.port.output.PatientPort;

import net.pladema.patient.repository.PatientRepository;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NewPatientPortImpl implements PatientPort {

	private final PatientRepository patientRepository;

	@Override
	public MedicationRequestValidationDispatcherPatientBo getPatientDataNeededForMedicationRequestValidation(Integer patientId) {
		return patientRepository.fetchPatientDataNeededForMedicationRequestValidation(patientId);
	}

}
