package net.pladema.emergencycare.application.port.output;

import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;

import java.time.LocalDateTime;

public interface HistoricEmergencyEpisodeStorage {

	HistoricEmergencyEpisodeBo create(HistoricEmergencyEpisodeBo historicEmergencyEpisodeBo);

	LocalDateTime getLatestByEmergencyCareEpisodeId(Integer episodeId);
}
