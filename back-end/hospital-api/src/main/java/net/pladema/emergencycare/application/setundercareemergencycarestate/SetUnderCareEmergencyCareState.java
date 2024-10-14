package net.pladema.emergencycare.application.setundercareemergencycarestate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeExcepcionEnum;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeException;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStateStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;
import net.pladema.emergencycare.application.port.output.HistoricEmergencyEpisodeStorage;

import net.pladema.emergencycare.domain.EmergencyCareEpisodeAttentionPlaceBo;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;

import net.pladema.establishment.controller.service.BedExternalService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SetUnderCareEmergencyCareState {

	private final EmergencyCareEpisodeStorage emergencyCareEpisodeStorage;
	private final EmergencyCareEpisodeStateStorage emergencyCareEpisodeStateStorage;
	private final HistoricEmergencyEpisodeStorage historicEmergencyEpisodeStorage;
	private final BedExternalService bedExternalService;

	@Transactional
	public Boolean run(Integer episodeId, Integer institutionId, EmergencyCareEpisodeAttentionPlaceBo eceap){
		log.debug("Input SetUnderCareEmergencyCareState parameters -> episodeId {}, institutionId {}, " +
				"emergencyCareEpisodeAttentionPlaceBo {}", episodeId, institutionId, eceap);
		validateStateChange(episodeId);
		Integer doctorsOfficeId = eceap.getDoctorsOfficeId();
		Integer shockroomId = eceap.getShockroomId();
		Integer bedId = eceap.getBedId();
		Short emergencyCareStateId = EEmergencyCareState.ATENCION.getId();

		if (doctorsOfficeId != null || shockroomId != null)
			validateAttentionPlace(doctorsOfficeId, shockroomId);

		saveHistoricEmergencyEpisode(episodeId, eceap);

		if (bedId != null) {
			validateBedStatus(bedId);
			emergencyCareEpisodeStateStorage.updateStateWithBed(episodeId, institutionId, emergencyCareStateId, bedId);
			bedExternalService.updateBedStatusOccupied(bedId);
		}

		if (shockroomId != null)
			emergencyCareEpisodeStateStorage.updateStateWithShockroom(episodeId, institutionId, emergencyCareStateId, shockroomId);

		if (doctorsOfficeId != null || (bedId == null && shockroomId == null))
			emergencyCareEpisodeStateStorage.updateStateWithDoctorsOffice(episodeId, institutionId, emergencyCareStateId, doctorsOfficeId);

		Boolean result = true;
		log.debug("Output -> {}", result);
		return result;
	}

	private void saveHistoricEmergencyEpisode(Integer episodeId, EmergencyCareEpisodeAttentionPlaceBo eceap) {
		HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo(episodeId, LocalDateTime.now(), EEmergencyCareState.ATENCION.getId(),
				eceap.getDoctorsOfficeId(), eceap.getShockroomId(), eceap.getBedId(), null);
		historicEmergencyEpisodeStorage.create(toSave);
	}

	private void validateStateChange(Integer episodeId){
		Optional<Short> fromStateOpt = emergencyCareEpisodeStateStorage.getEpisodeState(episodeId);

		if (fromStateOpt.isEmpty())
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.EPISODE_NOT_FOUND,
					"No se encontró un episodio de guardia con id " + episodeId);

		if (!EEmergencyCareState.validTransition(EEmergencyCareState.getById(fromStateOpt.get()),EEmergencyCareState.LLAMADO))
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.CHANGE_OF_STATE_NOT_VALID,
					"No es posible pasar a estado en atención desde el estado actual del episodio de guardia.");

		if (emergencyCareEpisodeStorage.existsDischargeForEpisode(episodeId))
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.EPISODE_ALREADY_DISCHARGED,
					"El episodio ya fue dado de alta.");
	}

	private void validateAttentionPlace(Integer doctorsOfficeId, Integer shockroomId) {
		log.debug("Input parameters validateAttentionPlace -> doctorsOfficeId {}, shockroomId {}", doctorsOfficeId, shockroomId);
		if (emergencyCareEpisodeStorage.existsEpisodeInOffice(doctorsOfficeId, shockroomId)) {
			if (doctorsOfficeId != null)
				throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.DOCTORS_OFFICE_NOT_AVAILABLE,
						"El consultorio elegido se encuentra ocupado.");
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.SHOCKROOM_NOT_AVAILABLE,
					"El shockroom elegido se encuentra ocupado.");
		}
	}

	private void validateBedStatus(Integer bedId) {
		log.debug("Input parameters validateBedStatus -> bedId {}", bedId);
		if (!bedExternalService.isBedFreeAndAvailable(bedId)) {
			throw new EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum.BED_NOT_AVAILABLE,
					"La cama seleccionada no está disponible.");
		}
	}
}
