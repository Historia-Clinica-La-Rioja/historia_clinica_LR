package net.pladema.patient.controller.service;

import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;

public interface PatientExternalMedicalCoverageService {

	PatientMedicalCoverageDto getCoverage(Integer patientMedicalCoverageId);
}
