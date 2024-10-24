package net.pladema.emergencycare.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeExcepcionEnum;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeException;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStateStorage;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmergencyCareEpisodeStateStorageImpl implements EmergencyCareEpisodeStateStorage {

	private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	@Override
	public Optional<Short> getEpisodeState(Integer episodeId) {
		return emergencyCareEpisodeRepository.getState(episodeId);
	}

	@Override
	public Boolean updateEpisodeState(Integer episodeId, EEmergencyCareState state) {
		EmergencyCareEpisode episode = getByIdOrFail(episodeId);
		episode.setEmergencyCareStateId(state.getId());
		emergencyCareEpisodeRepository.save(episode);
		return true;
	}

	@Override
	public void updateStateWithBed(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer bedId) {
		emergencyCareEpisodeRepository.updateStateWithBed(episodeId, institutionId, emergencyCareStateId, bedId);
	}

	@Override
	public void updateStateWithShockroom(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer shockroomId) {
		emergencyCareEpisodeRepository.updateStateWithShockroom(episodeId, institutionId, emergencyCareStateId, shockroomId);
	}

	@Override
	public void updateStateWithDoctorsOffice(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer doctorsOfficeId) {
		emergencyCareEpisodeRepository.updateState(episodeId, institutionId, emergencyCareStateId, doctorsOfficeId);
	}

	private EmergencyCareEpisode getByIdOrFail(Integer episodeId) {
		return emergencyCareEpisodeRepository.findById(episodeId).orElseThrow(
				() -> new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.EPISODE_NOT_FOUND,
						"No se encontró un episodio de guardia con id " + episodeId)
		);
	}
}
