package net.pladema.emergencycare.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeStateException;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeStateExceptionEnum;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.HistoricEmergencyEpisodeService;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.establishment.controller.service.BedExternalService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class EmergencyCareEpisodeStateServiceImpl implements EmergencyCareEpisodeStateService {

	private static final String WRONG_STATE_ID = "wrong-state-id";

	private static final String STATE_NOT_FOUND = "El estado del episodio de guardia no se encontró o no existe";

	private EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	private HistoricEmergencyEpisodeService historicEmergencyEpisodeService;

	private final BedExternalService bedExternalService;

	@Override
	public EEmergencyCareState getState(Integer episodeId, Integer institutionId) {
		log.debug("Input parameters -> episodeId {}, institutionId {}", episodeId, institutionId);
		Short emergencyCareStateid = emergencyCareEpisodeRepository.getState(episodeId)
				.orElseThrow(() -> new NotFoundException(WRONG_STATE_ID, STATE_NOT_FOUND));
		return EEmergencyCareState.getById(emergencyCareStateid);
	}

	@Override
	@Transactional
	public Boolean changeState(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer doctorsOfficeId, Integer shockroomId, Integer bedId) {
		log.debug("Input parameters -> episodeId {}, emergencyCareStateId {}, doctorsOfficeId {}, shockroomId {}, bedId {}",
				episodeId, emergencyCareStateId, doctorsOfficeId, shockroomId, bedId);
		assertEmergencyCareState(emergencyCareStateId, episodeId);
		if (emergencyCareStateId.equals(EEmergencyCareState.ESPERA.getId()) || emergencyCareStateId.equals(EEmergencyCareState.ALTA_MEDICA.getId()))
			freeOccupiedEmergencyCareSpace(episodeId, institutionId, emergencyCareStateId);
		if (emergencyCareStateId.equals(EEmergencyCareState.ALTA_ADMINISTRATIVA.getId())) {
			saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId);
			emergencyCareEpisodeRepository.updateState(episodeId, institutionId, emergencyCareStateId, null);
		}
		return true;
	}

	private void assertEmergencyCareState(Short emergencyCareStateId, Integer episodeId) {
		emergencyCareEpisodeRepository.getEpisode(episodeId).ifPresent(episode -> {
			if (emergencyCareStateId.equals(EEmergencyCareState.ESPERA.getId())
				&& episode.getEmergencyCareStateId().equals(EEmergencyCareState.ALTA_MEDICA.getId())) {
				throw new EmergencyCareEpisodeStateException(EmergencyCareEpisodeStateExceptionEnum.MEDICAL_DISCHARGE, "El episodio ya fue dado de alta médica");
			}

			if (emergencyCareStateId.equals(EEmergencyCareState.ESPERA.getId())
				&& episode.getEmergencyCareStateId().equals(EEmergencyCareState.ALTA_ADMINISTRATIVA.getId())) {
				throw new EmergencyCareEpisodeStateException(EmergencyCareEpisodeStateExceptionEnum.ADMINISTRATIVE_DISCHARGE, "El episodio ya fue dado de alta administrativa");
			}

			if (emergencyCareStateId.equals(EEmergencyCareState.ALTA_MEDICA.getId()) && episode.getEmergencyCareStateId().equals(EEmergencyCareState.ESPERA.getId())) {
				throw new EmergencyCareEpisodeStateException(EmergencyCareEpisodeStateExceptionEnum.WAITING_ROOM, "El episodio ha sido movido a sala de espera");
			}

			Boolean hasEvolutionNote = emergencyCareEpisodeRepository.episodeHasEvolutionNote(episodeId);

			if (emergencyCareStateId.equals(EEmergencyCareState.ALTA_ADMINISTRATIVA.getId()) &&
				episode.getEmergencyCareStateId().equals(EEmergencyCareState.ESPERA.getId()) &&
				hasEvolutionNote
			) {
				throw new EmergencyCareEpisodeStateException(EmergencyCareEpisodeStateExceptionEnum.WAITING_ROOM, "El episodio ha sido movido a sala de espera y tiene una nota de evolución asociada");
			}

			//Dest: ALTA_ADMINISTRATIVA
			//Current: AUSENTE (with evolution note)
			if (emergencyCareStateId.equals(EEmergencyCareState.ALTA_ADMINISTRATIVA.getId()) &&
					episode.getEmergencyCareStateId().equals(EEmergencyCareState.AUSENTE.getId()) &&
					hasEvolutionNote
			) {
				throw new EmergencyCareEpisodeStateException(EmergencyCareEpisodeStateExceptionEnum.WAITING_ROOM,
					"El episodio se encuentra en estado ausente y tiene una nota de evolución asociada");
			}

			//Dest: ALTA_ADMINISTRATIVA
			//Current: ATENCION
			if (emergencyCareStateId.equals(EEmergencyCareState.ALTA_ADMINISTRATIVA.getId()) &&
				episode.getEmergencyCareStateId().equals(EEmergencyCareState.ATENCION.getId())
			) {
				throw new EmergencyCareEpisodeStateException(
					EmergencyCareEpisodeStateExceptionEnum.ATTENTION,
					"El episodio se encuentra en atención");
			}

			//Dest: ALTA_ADMINISTRATIVA
			//Current: LLAMADO
			if (emergencyCareStateId.equals(EEmergencyCareState.ALTA_ADMINISTRATIVA.getId()) &&
					episode.getEmergencyCareStateId().equals(EEmergencyCareState.LLAMADO.getId())
			) {
				throw new EmergencyCareEpisodeStateException(
						EmergencyCareEpisodeStateExceptionEnum.CALLED,
						"El episodio se encuentra en estado llamado");
			}

		});
	}

	private void freeOccupiedEmergencyCareSpace(Integer episodeId, Integer institutionId, Short emergencyCareStateId) {
		Integer occupiedBedId = emergencyCareEpisodeRepository.getEmergencyCareEpisodeBedId(episodeId);
		if (occupiedBedId != null) {
			saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId);
			emergencyCareEpisodeRepository.updateStateWithBed(episodeId, institutionId, emergencyCareStateId, null);
			bedExternalService.freeBed(occupiedBedId);
			return;
		}
		Integer shockroomId = emergencyCareEpisodeRepository.getEmergencyCareEpisodeShockroomId(episodeId);
		if (shockroomId != null) {
			saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId);
			emergencyCareEpisodeRepository.updateStateWithShockroom(episodeId, institutionId, emergencyCareStateId, null);
			return;
		}
		Integer doctorsOfficeId = emergencyCareEpisodeRepository.getEmergencyCareEpisodeDoctorsOfficeId(episodeId);
		if (doctorsOfficeId != null || emergencyCareStateId.equals(EEmergencyCareState.ESPERA.getId())) {
			HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo(episodeId, emergencyCareStateId, null);
			emergencyCareEpisodeRepository.updateState(episodeId, institutionId, emergencyCareStateId, null);
			historicEmergencyEpisodeService.saveChange(toSave);
		}
	}

	private void saveHistoricEmergencyEpisode(Integer episodeId, Short emergencyCareStateId) {
		HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo();
		toSave.setEmergencyCareEpisodeId(episodeId);
		toSave.setEmergencyCareStateId(emergencyCareStateId);
		historicEmergencyEpisodeService.saveChange(toSave);
	}

}
