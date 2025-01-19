package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationCriticality;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationType;
import net.pladema.emergencycare.repository.EmergencyEpisodeAttendSectorType;
import net.pladema.emergencycare.service.EmergencyCareMasterDataService;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import net.pladema.establishment.domain.EBlockAttentionPlaceReason;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class EmergencyCareMasterDataServiceImpl implements EmergencyCareMasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareMasterDataServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    public EmergencyCareMasterDataServiceImpl(){
        super();
    }

    @Override
    public List<EEmergencyCareType> findAllType() {
        List<EEmergencyCareType> result = EEmergencyCareType.getAll();
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<EEmergencyCareEntrance> findAllEntrance() {
        List<EEmergencyCareEntrance> result =  EEmergencyCareEntrance.getAll();
        LOG.debug(OUTPUT, result);
        return result;
    }

	@Override
	public List<EmergencyEpisodeAttendSectorType> getEmergencyEpisodeSectorType() {
		List<EmergencyEpisodeAttendSectorType> result = EmergencyEpisodeAttendSectorType.getAll();
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<EEmergencyCareState> getEmergencyCareStates() {
		List<EEmergencyCareState> result = EEmergencyCareState.getAll();
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<EBlockAttentionPlaceReason> getAttentionPlaceBlockReasons() {
		var result = EBlockAttentionPlaceReason.getAll();
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<EIsolationType> getIsolationTypes() {
		var result = EIsolationType.getAll();
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<EIsolationCriticality> getCriticalities() {
		var result = EIsolationCriticality.getAll();
		LOG.debug(OUTPUT, result);
		return result;
	}
}
