package net.pladema.emergencycare.application.port.output;

import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;

import java.time.LocalDateTime;
import java.util.Optional;

public interface HistoricEmergencyEpisodeStorage {

	HistoricEmergencyEpisodeBo create(HistoricEmergencyEpisodeBo historicEmergencyEpisodeBo);

	LocalDateTime getLatestChangeStateDateByEpisodeId(Integer episodeId);

	Optional<HistoricEmergencyEpisodeBo> getLatestByEpisodeId(Integer episodeId);
}
