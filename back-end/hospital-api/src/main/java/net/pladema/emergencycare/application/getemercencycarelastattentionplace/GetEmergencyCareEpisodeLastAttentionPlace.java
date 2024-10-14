package net.pladema.emergencycare.application.getemercencycarelastattentionplace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.port.output.HistoricEmergencyEpisodeStorage;

import net.pladema.emergencycare.domain.EmergencyCareEpisodeAttentionPlaceBo;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetEmergencyCareEpisodeLastAttentionPlace {

	private final HistoricEmergencyEpisodeStorage historicEmergencyEpisodeStorage;

	public EmergencyCareEpisodeAttentionPlaceBo run(Integer episodeId){
		log.debug("Input GetEmergencyCareEpisodeLastAttentionPlace parameters -> episodeId {}", episodeId);
		EmergencyCareEpisodeAttentionPlaceBo result = historicEmergencyEpisodeStorage.getLatestAttentionPlaceByEpisodeId(episodeId);
		log.debug("Output -> {}", result);
		return result;
	}
}
