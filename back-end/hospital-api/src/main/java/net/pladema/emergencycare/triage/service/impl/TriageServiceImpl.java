package net.pladema.emergencycare.triage.service.impl;

import net.pladema.emergencycare.triage.repository.TriageRepository;
import net.pladema.emergencycare.triage.repository.entity.Triage;
import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TriageServiceImpl implements TriageService {

    private static final Logger LOG = LoggerFactory.getLogger(TriageServiceImpl.class);

    private final TriageRepository triageRepository;

    public TriageServiceImpl(TriageRepository triageRepository){
        super();
        this.triageRepository = triageRepository;
    }

    @Override
    public List<TriageBo> getAll(Integer episodeId) {
        //TODO implement method
        return null;
    }

    @Override
    public TriageBo createAdministrative(TriageBo triageBo) {
        LOG.debug("Input parameters -> triageBo {}", triageBo);
        Triage triage = triageRepository.save(new Triage(triageBo));
        triageBo.setId(triage.getId());
        LOG.debug("Output -> {}", triageBo);
        return triageBo;
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
