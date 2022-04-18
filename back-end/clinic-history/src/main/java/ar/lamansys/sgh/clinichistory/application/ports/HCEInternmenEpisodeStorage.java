package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;

import java.util.Optional;

public interface HCEInternmenEpisodeStorage {

	Optional<ExternalPatientCoverageDto> getActiveEpisodeMedicalCoverage(Integer internmentEpisodeId);

}