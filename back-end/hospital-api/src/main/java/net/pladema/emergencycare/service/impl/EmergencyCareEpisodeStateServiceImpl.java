package net.pladema.emergencycare.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.HistoricEmergencyEpisodeService;
import net.pladema.emergencycare.service.NotifyEmergencyCareSchedulerCallService;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.establishment.controller.service.BedExternalService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import java.util.Collections;

@Slf4j
@AllArgsConstructor
@Service
public class EmergencyCareEpisodeStateServiceImpl implements EmergencyCareEpisodeStateService {

	private static final String WRONG_STATE_ID = "wrong-state-id";

	private static final String STATE_NOT_FOUND = "El estado del episodio de guardia no se encontró o no existe";

	private static final String DOCTORS_OFFICE_NOT_AVAILABLE = "El consultorio elegido se encuentra ocupado";

	private static final String SHOCKROOM_NOT_AVAILABLE = "El shockroom elegido se encuentra ocupado";

	private EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	private HistoricEmergencyEpisodeService historicEmergencyEpisodeService;

	private final BedExternalService bedExternalService;

	private final NotifyEmergencyCareSchedulerCallService notifyEmergencyCareSchedulerCallService;

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
		if (emergencyCareStateId.equals(EEmergencyCareState.ATENCION.getId()))
			occupyEmergencyCareSpace(episodeId, institutionId, emergencyCareStateId, doctorsOfficeId, shockroomId, bedId);
		if (emergencyCareStateId.equals(EEmergencyCareState.ESPERA.getId()) || emergencyCareStateId.equals(EEmergencyCareState.ALTA_MEDICA.getId()))
			freeOccupiedEmergencyCareSpace(episodeId, institutionId, emergencyCareStateId);
		if (emergencyCareStateId.equals(EEmergencyCareState.ALTA_ADMINISTRATIVA.getId())) {
			saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId, null, null);
			emergencyCareEpisodeRepository.updateState(episodeId, institutionId, emergencyCareStateId, null);
		}
		return true;
	}

	private void freeOccupiedEmergencyCareSpace(Integer episodeId, Integer institutionId, Short emergencyCareStateId) {
		Integer occupiedBedId = emergencyCareEpisodeRepository.getEmergencyCareEpisodeBedId(episodeId);
		if (occupiedBedId != null) {
			saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId, null, null);
			emergencyCareEpisodeRepository.updateStateWithBed(episodeId, institutionId, emergencyCareStateId, null);
			bedExternalService.freeBed(occupiedBedId);
			return;
		}

		if (emergencyCareEpisodeRepository.getEmergencyCareEpisodeShockroomId(episodeId) != null) {
			saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId, null, null);
			emergencyCareEpisodeRepository.updateStateWithShockroom(episodeId, institutionId, emergencyCareStateId, null);
			return;
		}

		if (emergencyCareEpisodeRepository.getEmergencyCareEpisodeDoctorsOfficeId(episodeId) != null) {
			HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo(episodeId, emergencyCareStateId, null);
			emergencyCareEpisodeRepository.updateState(episodeId, institutionId, emergencyCareStateId, null);
			historicEmergencyEpisodeService.saveChange(toSave);
		}
	}

	private void occupyEmergencyCareSpace(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer doctorsOfficeId, Integer shockroomId, Integer bedId) {
		if (doctorsOfficeId != null || shockroomId != null)
			assertAttentionPlace(doctorsOfficeId, shockroomId);

		if (bedId != null) {
			if (emergencyCareStateId.equals(EEmergencyCareState.ATENCION.getId())) {
				saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId, bedId, null);
				emergencyCareEpisodeRepository.updateStateWithBed(episodeId, institutionId, emergencyCareStateId, bedId);
				bedExternalService.updateBedStatusOccupied(bedId);
			}
			if (emergencyCareStateId.equals(EEmergencyCareState.ESPERA.getId()) || emergencyCareStateId.equals(EEmergencyCareState.ALTA_MEDICA.getId())) {
				saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId, null, null);
				emergencyCareEpisodeRepository.updateStateWithBed(episodeId, institutionId, emergencyCareStateId, null);
				bedExternalService.freeBed(bedId);
			}
		}
		if (shockroomId != null) {
			if (emergencyCareStateId.equals(EEmergencyCareState.ATENCION.getId())) {
				saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId, null, shockroomId);
				emergencyCareEpisodeRepository.updateStateWithShockroom(episodeId, institutionId, emergencyCareStateId, shockroomId);
			}
			if (emergencyCareStateId.equals(EEmergencyCareState.ESPERA.getId()) || emergencyCareStateId.equals(EEmergencyCareState.ALTA_MEDICA.getId())) {
				saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId, null, null);
				emergencyCareEpisodeRepository.updateStateWithShockroom(episodeId, institutionId, emergencyCareStateId, null);
			}
		}
		if (doctorsOfficeId != null || (bedId == null && shockroomId == null)) {
			if (emergencyCareStateId.equals(EEmergencyCareState.ATENCION.getId())) {
				HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo(episodeId, emergencyCareStateId, doctorsOfficeId);
				emergencyCareEpisodeRepository.updateState(episodeId, institutionId, emergencyCareStateId, doctorsOfficeId);
				historicEmergencyEpisodeService.saveChange(toSave);
			}
			if (emergencyCareStateId.equals(EEmergencyCareState.ESPERA.getId()) || emergencyCareStateId.equals(EEmergencyCareState.ALTA_MEDICA.getId())) {
				HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo(episodeId, emergencyCareStateId, null);
				emergencyCareEpisodeRepository.updateState(episodeId, institutionId, emergencyCareStateId, null);
				historicEmergencyEpisodeService.saveChange(toSave);
			}
		}
		notifyEmergencyCareSchedulerCallService.run(episodeId);
	}

	private void saveHistoricEmergencyEpisode(Integer episodeId, Short emergencyCareStateId, Integer bedId, Integer shockroomId) {
		HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo();
		toSave.setEmergencyCareEpisodeId(episodeId);
		toSave.setEmergencyCareStateId(emergencyCareStateId);
		toSave.setBedId(bedId);
		toSave.setShockroomId(shockroomId);
		historicEmergencyEpisodeService.saveChange(toSave);
	}

	private void assertAttentionPlace(Integer doctorsOfficeId, Integer shockroomId) {
		log.debug("Input parameters -> doctorsOfficeId {}, shockroomId {}", doctorsOfficeId, shockroomId);
		if (emergencyCareEpisodeRepository.existsEpisodeInOffice(doctorsOfficeId, shockroomId) > 0)
			throw new ConstraintViolationException(doctorsOfficeId != null
					? DOCTORS_OFFICE_NOT_AVAILABLE
					: SHOCKROOM_NOT_AVAILABLE, Collections.emptySet());
	}
}
