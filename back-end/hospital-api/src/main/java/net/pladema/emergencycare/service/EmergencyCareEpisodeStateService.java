package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;

public interface EmergencyCareEpisodeStateService {

	EEmergencyCareState getState(Integer episodeId, Integer institutionId);

	Boolean changeState(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer doctorsOfficeId);
}
