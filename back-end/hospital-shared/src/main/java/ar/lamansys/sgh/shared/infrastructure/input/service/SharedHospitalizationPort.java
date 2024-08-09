package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SharedHospitalizationPort {

	Optional<ExternalPatientCoverageDto> getActiveEpisodeMedicalCoverage(Integer internmentEpisodeId);

	List<Integer> getInternmentEpisodeAllergies(Integer internmentEpisodeId);

	Optional<Integer> getInternmentEpisodeId(Integer institutionId, Integer patientId);

	Optional<BasicPatientDto> getPatientInfo(Integer internmentEpisodeId);

	LocalDate getEntryLocalDate(Integer internmentEpisodeId);

    Optional<Integer> getPatientMedicalCoverageId(Integer internmentEpisodeId);

	Boolean validateHospitalizationAnestheticReport(Long documentId, String reason);
}
