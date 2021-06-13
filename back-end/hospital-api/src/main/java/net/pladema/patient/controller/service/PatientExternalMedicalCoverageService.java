package net.pladema.patient.controller.service;

import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;

import java.util.List;

public interface PatientExternalMedicalCoverageService {

	PatientMedicalCoverageDto getCoverage(Integer patientMedicalCoverageId);

	List<PatientMedicalCoverageDto> getActivePrivateMedicalCoverages(Integer patientId);
}
