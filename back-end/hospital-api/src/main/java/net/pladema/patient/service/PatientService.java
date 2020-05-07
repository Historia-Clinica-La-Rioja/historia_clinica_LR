package net.pladema.patient.service;

import java.util.List;
import java.util.Optional;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.entity.Person;

public interface PatientService {

	List<PatientSearch> searchPatient(PatientSearchFilter searchFilter);

    Optional<Patient> getPatient(Integer patientId);
    
    Patient addPatient(Patient patientToSave);
    
    void federatePatient(Patient patient, Person person); 

}
