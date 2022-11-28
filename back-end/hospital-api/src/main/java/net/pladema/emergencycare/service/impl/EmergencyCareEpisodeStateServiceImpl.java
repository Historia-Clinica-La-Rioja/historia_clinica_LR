package net.pladema.emergencycare.service.impl;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.HistoricEmergencyEpisodeService;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EmergencyCareEpisodeStateServiceImpl implements EmergencyCareEpisodeStateService {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeStateServiceImpl.class);

	private static final String WRONG_STATE_ID = "wrong-state-id";

	private static final String STATE_NOT_FOUND = "El estado del episodio de guardia no se encontró o no existe";

	private static final String WRONG_CARE_ID_EPISODE = "wrong-care-id-episode";

	private static final String CARE_EPISODE_NOT_FOUND = "El episodio de guardia no se encontró o no existe";

	private EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	private final HistoricEmergencyEpisodeService historicEmergencyEpisodeService;

	public EmergencyCareEpisodeStateServiceImpl(EmergencyCareEpisodeRepository emergencyCareEpisodeRepository,
												HistoricEmergencyEpisodeService historicEmergencyEpisodeService){
		this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
		this.historicEmergencyEpisodeService = historicEmergencyEpisodeService;
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
	public Boolean changeState(Integer episodeId, Integer institutionId, Short emergencyCareStateId, Integer doctorsOfficeId) {
		LOG.debug("Input parameters -> episodeId {}, emergencyCareStateId {}, doctorsOfficeId {}",
				episodeId, emergencyCareStateId, doctorsOfficeId);
		HistoricEmergencyEpisodeBo toSave = new HistoricEmergencyEpisodeBo(episodeId, emergencyCareStateId, doctorsOfficeId);
		emergencyCareEpisodeRepository.updateState(episodeId, institutionId, emergencyCareStateId, doctorsOfficeId);
		historicEmergencyEpisodeService.saveChange(toSave);
		return true;
	}
}
