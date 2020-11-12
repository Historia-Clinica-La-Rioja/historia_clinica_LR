package net.pladema.patient.controller.service;

import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;

import java.util.Map;
import java.util.Set;

public interface PatientExternalService {

    BasicPatientDto getBasicDataFromPatient(Integer patientId);

    Map<Integer, BasicPatientDto> getBasicDataFromPatientsId(Set<Integer> patientId);

    PatientMedicalCoverageDto getCoverage(Integer patientMedicalCoverageId);
}
