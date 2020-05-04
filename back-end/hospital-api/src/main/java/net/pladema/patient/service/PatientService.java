package net.pladema.patient.service;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.PatientSearch;

import java.util.List;
import java.util.Optional;

public interface PatientService {

	List<PatientSearch> searchPatient(PatientSearchFilter searchFilter);

    Optional<Patient> getPatient(Integer patientId);
    
    Patient addPatient(Patient patientToSave);

}
