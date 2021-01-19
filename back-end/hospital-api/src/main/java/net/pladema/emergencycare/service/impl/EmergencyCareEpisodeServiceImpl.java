package net.pladema.emergencycare.service.impl;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeReasonRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.repository.PoliceInterventionRepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisodeReason;
import net.pladema.emergencycare.repository.entity.PoliceIntervention;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.PoliceInterventionBo;
import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import net.pladema.sgx.exceptions.NotFoundException;
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

    private static final String WRONG_CARE_ID_EPISODE = "wrong-care-id-episode";

    private static final String CARE_EPISODE_NOT_FOUND = "El episodio de guardia no se encontró o no existe";

    private final TriageService triageService;

    private final PoliceInterventionRepository policeInterventionRepository;

    private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

    private final EmergencyCareEpisodeReasonRepository emergencyCareEpisodeReasonRepository;

    public EmergencyCareEpisodeServiceImpl(TriageService triageService,
                                           PoliceInterventionRepository policeInterventionRepository,
                                           EmergencyCareEpisodeRepository emergencyCareEpisodeRepository,
                                           EmergencyCareEpisodeReasonRepository emergencyCareEpisodeReasonRepository){
        super();
        this.triageService = triageService;
        this.policeInterventionRepository = policeInterventionRepository;
        this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
        this.emergencyCareEpisodeReasonRepository = emergencyCareEpisodeReasonRepository;
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
    public EmergencyCareBo get(Integer episodeId, Integer institutionId) {
        LOG.debug("Input parameters -> episodeId {}, institutionId {}", episodeId, institutionId);
        EmergencyCareVo emergencyCareEpisode = emergencyCareEpisodeRepository.getEpisode(episodeId, institutionId)
                .orElseThrow(() -> new NotFoundException(WRONG_CARE_ID_EPISODE, CARE_EPISODE_NOT_FOUND));
        EmergencyCareBo result = new EmergencyCareBo(emergencyCareEpisode);
		LOG.debug(OUTPUT, result);
		return result;
    }


    @Override
    public EmergencyCareBo createAdministrative(EmergencyCareBo newEmergencyCare) {
        LOG.debug("Input parameters -> newEmergencyCare {}", newEmergencyCare);
        PoliceInterventionBo policeInterventionBo = newEmergencyCare.getPoliceIntervention();
        policeInterventionBo = (policeInterventionBo != null) ? savePoliceIntervention(newEmergencyCare.getPoliceIntervention()) : new PoliceInterventionBo();
        EmergencyCareBo emergencyCareEpisodeBo = saveEmergencyCareEpisode(newEmergencyCare, newEmergencyCare.getTriage(), policeInterventionBo.getId());
        TriageBo triageBo = saveTriageAdministrative(newEmergencyCare.getTriage(), emergencyCareEpisodeBo.getId());
        List<String> reasons = saveReasons(newEmergencyCare.getReasonIds(), emergencyCareEpisodeBo.getId());

        emergencyCareEpisodeBo.setPoliceIntervention(policeInterventionBo);
        emergencyCareEpisodeBo.setTriage(triageBo);
        emergencyCareEpisodeBo.setReasonIds(reasons);

        LOG.debug(OUTPUT, emergencyCareEpisodeBo);
        return emergencyCareEpisodeBo;
    }

    private TriageBo saveTriageAdministrative(TriageBo triageBo, Integer emergencyCareEpisodeId) {
        LOG.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}", triageBo, emergencyCareEpisodeId);
        triageBo.setEmergencyCareEpisodeId(emergencyCareEpisodeId);
        TriageBo result = triageService.createAdministrative(triageBo);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private PoliceInterventionBo savePoliceIntervention(PoliceInterventionBo policeInterventionBo) {
        LOG.debug("Input parameter -> policeInterventionBo {}", policeInterventionBo);
        PoliceIntervention policeIntervention = new PoliceIntervention(policeInterventionBo);
        policeIntervention = policeInterventionRepository.save(policeIntervention);
        PoliceInterventionBo result = new PoliceInterventionBo(policeIntervention);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private EmergencyCareBo saveEmergencyCareEpisode(EmergencyCareBo emergencyCareBo, TriageBo triageBo, Integer policeInterventionId) {
        LOG.debug("Input parameters -> emergencyCareBo {}, triageBo {}, policeInterventionId {}", emergencyCareBo, triageBo, policeInterventionId);
        EmergencyCareEpisode emergencyCareEpisode = new EmergencyCareEpisode(emergencyCareBo, triageBo, policeInterventionId);
        emergencyCareEpisode = emergencyCareEpisodeRepository.save(emergencyCareEpisode);
        EmergencyCareBo result = new EmergencyCareBo(emergencyCareEpisode);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private List<String> saveReasons(List<String> reasons, Integer emergencyCareEpisodeId) {
        LOG.debug("Input parameters -> reasons {}, emergencyCareEpisodeId {}", reasons, emergencyCareEpisodeId);
        reasons.forEach(reason -> {
            EmergencyCareEpisodeReason emergencyCareEpisodeReason = new EmergencyCareEpisodeReason(emergencyCareEpisodeId, reason);
            emergencyCareEpisodeReasonRepository.save(emergencyCareEpisodeReason);
        });
        LOG.debug(OUTPUT, reasons);
        return reasons;
    }

    @Override
    public EmergencyCareBo createAdult(EmergencyCareBo newEmergencyCare) {
        LOG.debug("Input parameters -> newEmergencyCare {}", newEmergencyCare);
        //TODO implement method
        return new EmergencyCareBo();
    }

    private TriageBo saveTriagePediatric(TriageBo triageBo, Integer emergencyCareEpisodeId) {
        LOG.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}", triageBo, emergencyCareEpisodeId);
        triageBo.setEmergencyCareEpisodeId(emergencyCareEpisodeId);
        TriageBo result = triageService.createPediatric(triageBo);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public EmergencyCareBo createPediatric(EmergencyCareBo newEmergencyCare) {
        LOG.debug("Input parameters -> newEmergencyCare {}", newEmergencyCare);
        PoliceInterventionBo policeInterventionBo = newEmergencyCare.getPoliceIntervention();
        policeInterventionBo = (policeInterventionBo != null) ? savePoliceIntervention(newEmergencyCare.getPoliceIntervention()) : new PoliceInterventionBo();
        EmergencyCareBo emergencyCareEpisodeBo = saveEmergencyCareEpisode(newEmergencyCare, newEmergencyCare.getTriage(), policeInterventionBo.getId());
        TriageBo triageBo = saveTriagePediatric(newEmergencyCare.getTriage(), emergencyCareEpisodeBo.getId());
        List<String> reasons = saveReasons(newEmergencyCare.getReasonIds(), emergencyCareEpisodeBo.getId());

        emergencyCareEpisodeBo.setPoliceIntervention(policeInterventionBo);
        emergencyCareEpisodeBo.setTriage(triageBo);
        emergencyCareEpisodeBo.setReasonIds(reasons);

        LOG.debug(OUTPUT, emergencyCareEpisodeBo);
        return emergencyCareEpisodeBo;
    }

    @Override
    public Boolean setPatient(Integer episodeId, Integer patientId) {
        LOG.debug("Input parameters -> episodeId {}, patientId {}",
                episodeId, patientId);
        emergencyCareEpisodeRepository.updatePatientId(episodeId, patientId);
        return true;
    }
}
