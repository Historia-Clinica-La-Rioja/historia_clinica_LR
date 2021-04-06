package net.pladema.patient.service;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.LimitedPatientSearchBo;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PatientService {

	List<PatientSearch> searchPatient(PatientSearchFilter searchFilter);

    LimitedPatientSearchBo searchPatientOptionalFilters(PatientSearchFilter searchFilter);

    Optional<Patient> getPatient(Integer patientId);

    List<Patient> getPatients(Set<Integer> patients);
    
    Patient addPatient(Patient patientToSave);
    
    boolean federatePatient(Patient patient, Person person);

    void federateAllValidatedPatients();
}
