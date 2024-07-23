package net.pladema.emergencycare.application.setabsentemergencycarestate;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeExcepcionEnum;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeException;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;

import net.pladema.emergencycare.application.port.output.HistoricEmergencyEpisodeStorage;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SetAbsentEmergencyCareState {

	private final EmergencyCareEpisodeStorage emergencyCareEpisodeStorage;
	private final HistoricEmergencyEpisodeStorage historicEmergencyEpisodeStorage;

	@Transactional
	public Boolean run(Integer episodeId){
		log.debug("Input SetAbsentEmergencyCareState parameters -> episodeId {}", episodeId);
		validateStateChange(episodeId);
		Boolean result = emergencyCareEpisodeStorage.updateEpisodeState(episodeId, EEmergencyCareState.AUSENTE);
		historicEmergencyEpisodeStorage.create(new HistoricEmergencyEpisodeBo(episodeId, EEmergencyCareState.AUSENTE.getId()));
		log.debug("Output -> {}", result);
		return result;
	}

	private void validateStateChange(Integer episodeId){
		Optional<Short> fromStateOpt = emergencyCareEpisodeStorage.getEpisodeState(episodeId);

		if (fromStateOpt.isEmpty())
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.EPISODE_NOT_FOUND,
					"No se encontró un episodio de guardia con id " + episodeId);

		if (!EEmergencyCareState.validTransition(EEmergencyCareState.getById(fromStateOpt.get()),EEmergencyCareState.AUSENTE))
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.CHANGE_OF_STATE_NOT_VALID,
					"No es posible pasar a estado ausente desde el estado actual del episodio de guardia.");

		if (emergencyCareEpisodeStorage.episodeHasEvolutionNote(episodeId))
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.CHANGE_OF_STATE_NOT_VALID,
					"No es posible pasar a estado ausente debido a que el paciente cuenta con una nota de evolución de guardia.");
	}
}
