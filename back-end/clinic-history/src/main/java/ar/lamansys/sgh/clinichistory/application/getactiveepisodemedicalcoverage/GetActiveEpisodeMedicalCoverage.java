package ar.lamansys.sgh.clinichistory.application.getactiveepisodemedicalcoverage;

import ar.lamansys.sgh.clinichistory.application.ports.HCEInternmenEpisodeStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetActiveEpisodeMedicalCoverage {

	private final HCEInternmenEpisodeStorage storage;

	public ExternalPatientCoverageDto run(Integer internmentEpisodeId){
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		ExternalPatientCoverageDto result = storage.getActiveEpisodeMedicalCoverage(internmentEpisodeId)
				.orElse(null);
		log.debug("Output -> {}", result);
		return result;
	}
}
