package net.pladema.emergencycare.service.impl;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.HistoricEmergencyEpisodeService;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.establishment.controller.service.BedExternalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import java.util.Collections;


@Service
public class EmergencyCareEpisodeStateServiceImpl implements EmergencyCareEpisodeStateService {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeStateServiceImpl.class);

	private static final String WRONG_STATE_ID = "wrong-state-id";

	private static final String STATE_NOT_FOUND = "El estado del episodio de guardia no se encontró o no existe";

	private static final String WRONG_CARE_ID_EPISODE = "wrong-care-id-episode";

	private static final String CARE_EPISODE_NOT_FOUND = "El episodio de guardia no se encontró o no existe";

	private static final String DOCTORS_OFFICE_NOT_AVAILABLE = "El consultorio elegido se encuentra ocupado";

	private static final String SHOCKROOM_NOT_AVAILABLE = "El shockroom elegido se encuentra ocupado";

	private EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	private final HistoricEmergencyEpisodeService historicEmergencyEpisodeService;

	private final BedExternalService bedExternalService;

	public EmergencyCareEpisodeStateServiceImpl(EmergencyCareEpisodeRepository emergencyCareEpisodeRepository,
												HistoricEmergencyEpisodeService historicEmergencyEpisodeService,
												BedExternalService bedExternalService){
		this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
		this.historicEmergencyEpisodeService = historicEmergencyEpisodeService;
		this.bedExternalService = bedExternalService;
	}

	@Override
	public EEmergencyCareState getState(Integer episodeId, Integer institutionId) {
		LOG.debug("Input parameters -> episodeId {}, institutionId {}", episodeId, institutionId);
		Short emergencyCareStateid = emergencyCareEpisodeRepository.getState(episodeId, institutionId)
				.orElseThrow(() -> new NotFoundException(WRONG_STATE_ID, STATE_NOT_FOUND));
		return EEmergencyCareState.getById(emergencyCareStateid);
	}

	@Override
	@Transactional
	public Boolean changeState(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer doctorsOfficeId, Integer shockroomId, Integer bedId) {
		LOG.debug("Input parameters -> episodeId {}, emergencyCareStateId {}, doctorsOfficeId {}, shockroomId {}, bedId {}",
				episodeId, emergencyCareStateId, doctorsOfficeId, shockroomId, bedId);

		if (doctorsOfficeId != null || shockroomId != null)
			assertAttentionPlace(doctorsOfficeId, shockroomId);

		if (bedId != null) {
			saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId, bedId);
			emergencyCareEpisodeRepository.updateStateWithBed(episodeId, institutionId, emergencyCareStateId, bedId);
			bedExternalService.updateBedStatusOccupied(bedId);
		}
		if (shockroomId != null) {
			saveHistoricEmergencyEpisode(episodeId, emergencyCareStateId, shockroomId);
			emergencyCareEpisodeRepository.updateStateWithShockroom(episodeId, institutionId, emergencyCareStateId, shockroomId);
		}
		if (doctorsOfficeId != null || (bedId == null && shockroomId == null)) {
			HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo(episodeId, emergencyCareStateId, doctorsOfficeId);
			emergencyCareEpisodeRepository.updateState(episodeId, institutionId, emergencyCareStateId, doctorsOfficeId);
			historicEmergencyEpisodeService.saveChange(toSave);
		}
		return true;
	}

	private void saveHistoricEmergencyEpisode(Integer episodeId, Short emergencyCareStateId, Integer placeId) {
		HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo();
		toSave.setEmergencyCareEpisodeId(episodeId);
		toSave.setEmergencyCareStateId(emergencyCareStateId);
		toSave.setBedId(placeId);
		historicEmergencyEpisodeService.saveChange(toSave);
	}

	private void assertAttentionPlace(Integer doctorsOfficeId, Integer shockroomId) {
		LOG.debug("Input parameters -> doctorsOfficeId {}, shockroomId {}", doctorsOfficeId, shockroomId);
		if (emergencyCareEpisodeRepository.existsEpisodeInOffice(doctorsOfficeId, shockroomId) > 0)
			throw new ConstraintViolationException(doctorsOfficeId != null
					? DOCTORS_OFFICE_NOT_AVAILABLE
					: SHOCKROOM_NOT_AVAILABLE, Collections.emptySet());
	}
}
