package net.pladema.emergencycare.application.hasepisodeadministrativedischarge;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.service.EmergencyCareEpisodeAdministrativeDischargeService;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class HasEpisodeAdministrativeDischarge {

	private final EmergencyCareEpisodeAdministrativeDischargeService emergencyCareEpisodeAdministrativeDischargeService;

	public Boolean run (Integer episodeId) {
		log.debug("Input parameters -> episodeId {}", episodeId);
		Boolean result = emergencyCareEpisodeAdministrativeDischargeService.hasAdministrativeDischarge(episodeId);
		log.debug("Output -> result {}", result);
		return result;
	}

}
