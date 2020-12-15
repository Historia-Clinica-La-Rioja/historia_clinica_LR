package net.pladema.emergencycare.service.impl;

import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmergencyCareEpisodeServiceImpl implements EmergencyCareEpisodeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeServiceImpl.class);

    public EmergencyCareEpisodeServiceImpl(){
        super();
    }

    @Override
    public List<EmergencyCareBo> getAll(Integer institutionId) {
        LOG.debug("Input parameters -> institutionId {}", institutionId);
        //TODO implement method
        return new ArrayList<>();
    }

    @Override
    public EmergencyCareBo get(Integer episodeId) {
        LOG.debug("Input parameters -> episodeId {}", episodeId);
        //TODO implement method
        return new EmergencyCareBo();
    }

    @Override
    public EmergencyCareBo createAdministrative(EmergencyCareBo newEmergencyCare) {
        LOG.debug("Input parameters -> newEmergencyCare {}", newEmergencyCare);
        //TODO implement method
        return new EmergencyCareBo();
    }

    @Override
    public EmergencyCareBo createAdult(EmergencyCareBo newEmergencyCare) {
        LOG.debug("Input parameters -> newEmergencyCare {}", newEmergencyCare);
        //TODO implement method
        return new EmergencyCareBo();
    }

    @Override
    public EmergencyCareBo createPediatric(EmergencyCareBo newEmergencyCare) {
        LOG.debug("Input parameters -> newEmergencyCare {}", newEmergencyCare);
        //TODO implement method
        return new EmergencyCareBo();
    }

    @Override
    public MasterDataProjection getState(Integer episodeId) {
        LOG.debug("Input parameters -> episodeId {}", episodeId);
        //TODO implement method
        return null;
    }

    @Override
    public Boolean changeState(Integer episodeId, Short emergencyCareStateId, Integer doctorsOfficeId) {
        LOG.debug("Input parameters -> episodeId {}, emergencyCareStateId {}, doctorsOfficeId {}",
                episodeId, emergencyCareStateId, doctorsOfficeId);
        //TODO implement method
        return true;
    }
}
