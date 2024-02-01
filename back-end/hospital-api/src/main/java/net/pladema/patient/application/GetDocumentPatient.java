package net.pladema.patient.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.patient.domain.DocumentPatientBo;

import net.pladema.patient.repository.PatientRepository;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class GetDocumentPatient {

	private PatientRepository patientRepository;

	public DocumentPatientBo run(Integer patientId) {
		log.debug("Input parameters -> patientId {}", patientId);
		DocumentPatientBo result = patientRepository.getDocumentPatientData(patientId);
		log.debug("Output -> {}", result);
		return result;
	}

}
