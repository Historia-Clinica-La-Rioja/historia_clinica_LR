package net.pladema.emergencycare.application.port.output;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;

import java.util.Optional;

public interface EmergencyCareEpisodeStorage {

	Optional<Short> getEpisodeState(Integer episodeId);

	Boolean updateEpisodeState(Integer episodeId, EEmergencyCareState state);

	Boolean episodeHasEvolutionNote(Integer episodeId);
}
