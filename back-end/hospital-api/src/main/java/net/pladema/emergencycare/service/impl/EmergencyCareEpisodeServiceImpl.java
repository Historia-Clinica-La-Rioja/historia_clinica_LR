package net.pladema.emergencycare.service.impl;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmergencyCareEpisodeServiceImpl implements EmergencyCareEpisodeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

    public EmergencyCareEpisodeServiceImpl(EmergencyCareEpisodeRepository emergencyCareEpisodeRepository){
        super();
        this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
    }

    @Override
    public List<EmergencyCareBo> getAll(Integer institutionId) {
        LOG.debug("Input parameters -> institutionId {}", institutionId);
        List<EmergencyCareVo> resultQuery = emergencyCareEpisodeRepository.getAll(institutionId);
        List<EmergencyCareBo> result = resultQuery.stream().map(EmergencyCareBo::new)
                .sorted(Comparator.comparing(EmergencyCareBo::getEmergencyCareState).thenComparing(EmergencyCareBo::getTriageCategoryId).thenComparing(EmergencyCareBo::getCreatedOn))
                        .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
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

    @Override
    public Boolean setPatient(Integer episodeId, Integer patientId) {
        LOG.debug("Input parameters -> episodeId {}, patientId {}",
                episodeId, patientId);
        //TODO implement method
        return true;
    }
}
