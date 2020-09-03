package net.pladema.patient.controller.service;

import net.pladema.patient.controller.dto.BasicPatientDto;

public interface PatientExternalService {

    BasicPatientDto getBasicDataFromPatient(Integer patientId);

}
