package net.pladema.emergencycare.infrastructure;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeExcepcionEnum;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeException;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;

import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmergencyCareEpisodeStorageImpl implements EmergencyCareEpisodeStorage {

	private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	@Override
	public EmergencyCareEpisode getByIdOrFail(Integer episodeId) {
		return emergencyCareEpisodeRepository.findById(episodeId).orElseThrow(
				() -> new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.EPISODE_NOT_FOUND,
						"No se encontró un episodio de guardia con id " + episodeId)
		);
	}

	@Override
	public EmergencyCareEpisode save(EmergencyCareEpisode episode) {
		return emergencyCareEpisodeRepository.save(episode);
	}
}
