package net.pladema.patient.service;

import java.util.List;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientSearch;

public interface PatientService {

	public List<PatientSearch> searchPatient(PatientSearchFilter searchFilter); 
	
}
