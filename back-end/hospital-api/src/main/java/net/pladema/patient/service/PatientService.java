package net.pladema.patient.service;

import java.util.List;
import java.util.Optional;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.PatientSearch;

public interface PatientService {

	public List<PatientSearch> searchPatient(PatientSearchFilter searchFilter);

    Optional<Patient> getPatient(Integer patientId);
}
