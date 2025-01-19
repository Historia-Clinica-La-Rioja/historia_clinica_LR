package net.pladema.patient.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCarePatientStorage;

import net.pladema.emergencycare.domain.EmergencyCarePatientBo;

import net.pladema.patient.repository.PatientRepository;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmergencyCarePatientStorageImpl implements EmergencyCarePatientStorage {

	private final PatientRepository patientRepository;

	@Override
	public EmergencyCarePatientBo getById(Integer id) {
		return patientRepository.findEmergencyCarePatientById(id);
	}
}
