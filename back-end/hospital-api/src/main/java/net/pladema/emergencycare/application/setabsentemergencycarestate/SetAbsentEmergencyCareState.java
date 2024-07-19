package net.pladema.emergencycare.application.setabsentemergencycarestate;

import lombok.RequiredArgsConstructor;

import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeExcepcionEnum;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeException;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;

import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class SetAbsentEmergencyCareState {

	private final EmergencyCareEpisodeStorage emergencyCareEpisodeStorage;

	@Transactional
	public Boolean run(Integer episodeId){
		EmergencyCareEpisode episode = emergencyCareEpisodeStorage.getByIdOrFail(episodeId);
		validateStateChange(episode.getEmergencyCareStateId());
		episode.setEmergencyCareStateId(EEmergencyCareState.AUSENTE.getId());
		episode.setStateUpdatedOn(LocalDateTime.now());
		emergencyCareEpisodeStorage.save(episode);
		return true;
	}

	private void validateStateChange(Short emergencyCareStateId){
		EEmergencyCareState fromState = EEmergencyCareState.getById(emergencyCareStateId);
		if (!EEmergencyCareState.validTransition(fromState,EEmergencyCareState.AUSENTE))
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.CHANGE_OF_STATE_NOT_VALID,
					"No es posible pasar a estado ausente desde el estado actual del episodio de guardia.");
	}
}
