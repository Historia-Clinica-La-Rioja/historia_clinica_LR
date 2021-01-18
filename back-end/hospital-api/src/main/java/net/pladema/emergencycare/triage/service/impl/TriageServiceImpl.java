package net.pladema.emergencycare.triage.service.impl;

import net.pladema.emergencycare.triage.repository.TriageDetailsRepository;
import net.pladema.emergencycare.triage.repository.TriageRepository;
import net.pladema.emergencycare.triage.repository.TriageVitalSignsRepository;
import net.pladema.emergencycare.triage.repository.entity.Triage;
import net.pladema.emergencycare.triage.repository.entity.TriageDetails;
import net.pladema.emergencycare.triage.repository.entity.TriageVitalSigns;
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

    private final TriageDetailsRepository triageDetailsRepository;

    private final TriageVitalSignsRepository triageVitalSignsRepository;

    public TriageServiceImpl(TriageRepository triageRepository,
                             TriageDetailsRepository triageDetailsRepository,
                             TriageVitalSignsRepository triageVitalSignsRepository){
        super();
        this.triageRepository = triageRepository;
        this.triageDetailsRepository = triageDetailsRepository;
        this.triageVitalSignsRepository = triageVitalSignsRepository;
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
    public TriageBo createPediatric(TriageBo triageBo) {
        LOG.debug("Input parameter -> triageBo {}", triageBo);
        Triage triage = triageRepository.save(new Triage(triageBo));
        triageBo.setId(triage.getId());
        if (existDetails(triageBo))
            triageDetailsRepository.save(new TriageDetails(triageBo));
        saveVitalSigns(triageBo.getId(), triageBo.getVitalSignIds());
        LOG.debug("Output -> {}", triageBo);
        return triageBo;
    }

    private boolean existDetails(TriageBo triageBo) {
        LOG.debug("Input parameter -> triageBo {}", triageBo);
        boolean result =  (triageBo.getBodyTemperatureId() != null) ||
                (triageBo.getCryingExcessive() != null) ||
                (triageBo.getMuscleHypertoniaId() != null) ||
                (triageBo.getRespiratoryRetractionId() != null) ||
                (triageBo.getStridor() != null) ||
                (triageBo.getPerfusionId() != null);
        LOG.debug("Output -> {}", result);
        return result;
    }

    private void saveVitalSigns(Integer triageId, List<Integer> vitalSignIds) {
        LOG.debug("Input parameters -> triageId {}, vitalSignIds {}", triageId, vitalSignIds);
        vitalSignIds.forEach(id -> triageVitalSignsRepository.save(new TriageVitalSigns(triageId, id)));
    }
}
