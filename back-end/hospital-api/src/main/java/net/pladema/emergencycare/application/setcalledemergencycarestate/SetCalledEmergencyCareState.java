package net.pladema.emergencycare.application.setcalledemergencycarestate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeExcepcionEnum;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeException;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStateStorage;
import net.pladema.emergencycare.application.port.output.HistoricEmergencyEpisodeStorage;
import net.pladema.emergencycare.domain.EmergencyCareEpisodeAttentionPlaceBo;
import net.pladema.emergencycare.service.NotifyEmergencyCareSchedulerCallService;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SetCalledEmergencyCareState {

	private final EmergencyCareEpisodeStateStorage emergencyCareEpisodeStateStorage;
	private final HistoricEmergencyEpisodeStorage historicEmergencyEpisodeStorage;
	private final NotifyEmergencyCareSchedulerCallService notifyEmergencyCareSchedulerCallService;
	private static final Short INITIAL_CALLS_COUNT = 1;

	@Transactional
	public Boolean run(Integer episodeId, Integer institutionId, EmergencyCareEpisodeAttentionPlaceBo emergencyCareEpisodeAttentionPlaceBo){
		log.debug("Input SetCalledEmergencyCareState parameters -> episodeId {}", episodeId);
		validateStateChange(episodeId);
		Optional<HistoricEmergencyEpisodeBo> hee = historicEmergencyEpisodeStorage.getLatestByEpisodeId(episodeId);
		if (hee.isPresent() && hee.get().getEmergencyCareStateId().equals(EEmergencyCareState.LLAMADO.getId()))
			saveHistoricEmergencyEpisode(episodeId, emergencyCareEpisodeAttentionPlaceBo, (short) (hee.get().getCalls() + 1));
		else saveHistoricEmergencyEpisode(episodeId, emergencyCareEpisodeAttentionPlaceBo, INITIAL_CALLS_COUNT);
		Boolean result = emergencyCareEpisodeStateStorage.updateEpisodeState(episodeId, EEmergencyCareState.LLAMADO);
		notifyEmergencyCareSchedulerCallService.run(episodeId, institutionId);
		log.debug("Output -> {}", result);
		return result;
	}

	private void saveHistoricEmergencyEpisode(Integer episodeId, EmergencyCareEpisodeAttentionPlaceBo eceap, Short calls) {
		HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo(episodeId, LocalDateTime.now(),EEmergencyCareState.LLAMADO.getId(),
				eceap.getDoctorsOfficeId(), eceap.getShockroomId(), eceap.getBedId(), calls);
		historicEmergencyEpisodeStorage.create(toSave);
	}

	private void validateStateChange(Integer episodeId){
		Optional<Short> fromStateOpt = emergencyCareEpisodeStateStorage.getEpisodeState(episodeId);

		if (fromStateOpt.isEmpty())
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.EPISODE_NOT_FOUND,
					"No se encontró un episodio de guardia con id " + episodeId);

		if (!EEmergencyCareState.validTransition(EEmergencyCareState.getById(fromStateOpt.get()),EEmergencyCareState.ATENCION))
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.CHANGE_OF_STATE_NOT_VALID,
					"No es posible pasar a estado llamado desde el estado actual del episodio de guardia.");

	}
}
