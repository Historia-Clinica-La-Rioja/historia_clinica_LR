package net.pladema.patient.service;

import net.pladema.patient.service.domain.ItsCoveredResponseBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

import java.util.List;
import java.util.Optional;

public interface PatientMedicalCoverageService {

    List<PatientMedicalCoverageBo> getActiveCoverages(Integer patientId);

	PatientMedicalCoverageBo getActiveCoveragesByOrderId(Integer orderId);

	Optional<PatientMedicalCoverageBo> getCoverage(Integer patientMedicalCoverageId);

    List<PatientMedicalCoverageBo> getActiveHealthInsurances(Integer patientId);

    List<PatientMedicalCoverageBo> getActivePrivateHealthInsurances(Integer patientId);

    List<Integer> saveCoverages(List<PatientMedicalCoverageBo> coverages, Integer patientId);

    List<Integer> saveExternalCoverages(List<PatientMedicalCoverageBo> coverages, Integer patientId);

	List<Integer> toModifyAppointmentCoverage(List<PatientMedicalCoverageBo> patientMedicalCoverages);

	ItsCoveredResponseBo itsCovered(Integer institutionId, Integer coverageId, Integer healthcareProfessionalId);
}
