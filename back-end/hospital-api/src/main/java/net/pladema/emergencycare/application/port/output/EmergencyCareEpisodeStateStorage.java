package net.pladema.emergencycare.application.port.output;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;

import java.util.Optional;

public interface EmergencyCareEpisodeStateStorage {

	Optional<Short> getEpisodeState(Integer episodeId);

	Boolean updateEpisodeState(Integer episodeId, EEmergencyCareState state);

	void updateStateWithBed(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer bedId);

	void updateStateWithShockroom(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer shockroomId);

	void updateStateWithDoctorsOffice(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer doctorsOfficeId);
}
