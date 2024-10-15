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

import net.pladema.establishment.application.attentionplaces.FetchAttentionPlaceBlockStatus;

import net.pladema.establishment.domain.FetchAttentionPlaceBlockStatusBo;

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
	private final FetchAttentionPlaceBlockStatus fetchAttentionPlaceBlockStatus;
	private static final Short INITIAL_CALLS_COUNT = 1;


	@Transactional
	public Boolean run(Integer episodeId, Integer institutionId, EmergencyCareEpisodeAttentionPlaceBo emergencyCareEpisodeAttentionPlaceBo){
		log.debug("Input SetCalledEmergencyCareState parameters -> episodeId {}", episodeId);
		validateStateChange(episodeId);
		validateAttentionPlaceStatus(institutionId, emergencyCareEpisodeAttentionPlaceBo);
		Optional<HistoricEmergencyEpisodeBo> hee = historicEmergencyEpisodeStorage.getLatestByEpisodeId(episodeId);
		if (hee.isPresent() && hee.get().getEmergencyCareStateId().equals(EEmergencyCareState.LLAMADO.getId()))
			saveHistoricEmergencyEpisode(episodeId, emergencyCareEpisodeAttentionPlaceBo, (short) (hee.get().getCalls() + 1));
		else saveHistoricEmergencyEpisode(episodeId, emergencyCareEpisodeAttentionPlaceBo, INITIAL_CALLS_COUNT);
		Boolean result = updateEpisodeState(episodeId, institutionId, emergencyCareEpisodeAttentionPlaceBo);
		notifyEmergencyCareSchedulerCallService.run(episodeId, institutionId);
		log.debug("Output -> {}", result);
		return result;
	}

	private void saveHistoricEmergencyEpisode(Integer episodeId, EmergencyCareEpisodeAttentionPlaceBo eceap, Short calls) {
		HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo(episodeId, LocalDateTime.now(),EEmergencyCareState.LLAMADO.getId(),
				eceap.getDoctorsOfficeId(), eceap.getShockroomId(), eceap.getBedId(), calls);
		historicEmergencyEpisodeStorage.create(toSave);
	}

	private void validateAttentionPlaceStatus(Integer institutionId, EmergencyCareEpisodeAttentionPlaceBo attentionPlace) {
		if (attentionPlace.isBed())
			 checkBlocked(fetchAttentionPlaceBlockStatus.findForBed(institutionId, attentionPlace.getBedId()));
		if (attentionPlace.isDoctorsOffice())
			checkBlocked(fetchAttentionPlaceBlockStatus.findForDoctorsOffice(institutionId, attentionPlace.getDoctorsOfficeId()));
		if (attentionPlace.isShockRoom())
			checkBlocked(fetchAttentionPlaceBlockStatus.findForShockRoom(institutionId, attentionPlace.getShockroomId()));
	}

	private void checkBlocked(Optional<FetchAttentionPlaceBlockStatusBo> status) {
		if(status.map(x -> x.getIsBlocked()).orElse(false))
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.BLOCKED,
					"El lugar de atención se encuentra bloqueado.");
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

	private Boolean updateEpisodeState(Integer episodeId, Integer institutionId, EmergencyCareEpisodeAttentionPlaceBo emergencyCareEpisodeAttentionPlaceBo){
		Integer doctorsOfficeId = emergencyCareEpisodeAttentionPlaceBo.getDoctorsOfficeId();
		Integer bedId = emergencyCareEpisodeAttentionPlaceBo.getBedId();
		Integer shockroomId = emergencyCareEpisodeAttentionPlaceBo.getShockroomId();
		Short stateId = EEmergencyCareState.LLAMADO.getId();
		if (doctorsOfficeId != null)
			emergencyCareEpisodeStateStorage.updateStateWithDoctorsOffice(episodeId,institutionId,stateId,doctorsOfficeId);
		else if (bedId != null)
			emergencyCareEpisodeStateStorage.updateStateWithBed(episodeId,institutionId,stateId,bedId);
		else if (shockroomId != null)
			emergencyCareEpisodeStateStorage.updateStateWithShockroom(episodeId,institutionId,stateId,shockroomId);
		return true;
	}
}
