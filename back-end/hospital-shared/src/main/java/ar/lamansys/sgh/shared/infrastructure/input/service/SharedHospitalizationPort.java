package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.Optional;

public interface SharedHospitalizationPort {

	Optional<ExternalPatientCoverageDto> getActiveEpisodeMedicalCoverage(Integer internmentEpisodeId);
}
