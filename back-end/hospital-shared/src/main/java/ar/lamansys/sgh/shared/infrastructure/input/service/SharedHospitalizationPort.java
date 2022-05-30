package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;
import java.util.Optional;

public interface SharedHospitalizationPort {

	Optional<ExternalPatientCoverageDto> getActiveEpisodeMedicalCoverage(Integer internmentEpisodeId);

	List<Integer> getInternmentEpisodeAllergies(Integer internmentEpisodeId);

	Optional<Integer> getInternmentEpisodeId(Integer institutionId, Integer patientId);

}
