package net.pladema.emergencycare.application.port.output;

import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;

public interface EmergencyCareEpisodeStorage {

	EmergencyCareEpisode getByIdOrFail(Integer episodeId);

	EmergencyCareEpisode save(EmergencyCareEpisode episode);
}
