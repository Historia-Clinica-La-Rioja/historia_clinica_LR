package ar.lamansys.sgh.clinichistory.application.fetchEmergencyCareEpisodeState;

import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.emergencyCareEpisodeState.EmergencyCareClinicalObservationsRepository;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
@Slf4j
@Service
@AllArgsConstructor
public class FetchECStateClinicalObservations {

	private final EmergencyCareClinicalObservationsRepository clinicalObservationsRepository;

	public RiskFactorBo getLastRiskFactorsGeneralState(Integer episodeId) {
		log.debug("Input parameters -> episodeId {}", episodeId);
		MapClinicalObservationVo resultQuery = clinicalObservationsRepository.getGeneralState(episodeId);
		RiskFactorBo result = resultQuery.getLastRiskFactors().orElse(null);
		log.debug("OUTPUT -> {}", result);
		return result;
	}
}
