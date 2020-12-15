package net.pladema.emergencycare.triage.service.impl;

import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TriageServiceImpl implements TriageService {

    private static final Logger LOG = LoggerFactory.getLogger(TriageServiceImpl.class);

    public TriageServiceImpl(){
        super();
    }

    @Override
    public List<TriageBo> getAll(Integer episodeId) {
        //TODO implement method
        return null;
    }

    @Override
    public TriageBo createAdministrative(TriageBo triage) {
        //TODO implement method
        return null;
    }

    @Override
    public TriageBo createAdultGynecological(TriageBo triage) {
        //TODO implement method
        return null;
    }

    @Override
    public TriageBo createPediatric(TriageBo triage) {
        //TODO implement method
        return null;
    }
}
