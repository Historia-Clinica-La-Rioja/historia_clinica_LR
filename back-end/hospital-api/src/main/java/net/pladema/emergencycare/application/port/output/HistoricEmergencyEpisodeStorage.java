package net.pladema.emergencycare.application.port.output;

import net.pladema.emergencycare.domain.EmergencyCareEpisodeAttentionPlaceBo;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;

import java.util.Optional;

public interface HistoricEmergencyEpisodeStorage {

	HistoricEmergencyEpisodeBo create(HistoricEmergencyEpisodeBo historicEmergencyEpisodeBo);

	Optional<HistoricEmergencyEpisodeBo> getLatestByEpisodeId(Integer episodeId);

	EmergencyCareEpisodeAttentionPlaceBo getLatestAttentionPlaceByEpisodeId(Integer episodeId);
}
