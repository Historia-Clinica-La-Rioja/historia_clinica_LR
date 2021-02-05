package net.pladema.emergencycare.service.impl;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeReasonRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.repository.PoliceInterventionRepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisodeReason;
import net.pladema.emergencycare.repository.entity.PoliceIntervention;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.PatientECEBo;
import net.pladema.emergencycare.service.domain.PoliceInterventionBo;
import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;
import net.pladema.sgx.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
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

    private final InstitutionExternalService institutionExternalService;

    public EmergencyCareEpisodeServiceImpl(TriageService triageService,
                                           PoliceInterventionRepository policeInterventionRepository,
                                           EmergencyCareEpisodeRepository emergencyCareEpisodeRepository,
                                           EmergencyCareEpisodeReasonRepository emergencyCareEpisodeReasonRepository,
                                           InstitutionExternalService institutionExternalService){
        super();
        this.triageService = triageService;
        this.policeInterventionRepository = policeInterventionRepository;
        this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
        this.emergencyCareEpisodeReasonRepository = emergencyCareEpisodeReasonRepository;
        this.institutionExternalService = institutionExternalService;
    }

    @Override
    public List<EmergencyCareBo> getAll(Integer institutionId) {
        LOG.debug("Input parameters -> institutionId {}", institutionId);
        List<EmergencyCareVo> resultQuery = emergencyCareEpisodeRepository.getAll(institutionId);
        List<EmergencyCareBo> result = resultQuery.stream().map(EmergencyCareBo::new)
                .sorted(Comparator.comparing(EmergencyCareBo::getEmergencyCareStateId).thenComparing(EmergencyCareBo::getTriageCategoryId).thenComparing(EmergencyCareBo::getCreatedOn))
                        .collect(Collectors.toList());
        result.forEach(ec -> ec.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, ec.getCreatedOn())));
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public EmergencyCareBo get(Integer episodeId, Integer institutionId) {
        LOG.debug("Input parameters -> episodeId {}, institutionId {}", episodeId, institutionId);
        EmergencyCareVo emergencyCareEpisode = emergencyCareEpisodeRepository.getEpisode(episodeId, institutionId)
                .orElseThrow(() -> new NotFoundException(WRONG_CARE_ID_EPISODE, CARE_EPISODE_NOT_FOUND));
        EmergencyCareBo result = new EmergencyCareBo(emergencyCareEpisode);

        //Motivos de consulta
        List<ReasonBo> reasons = emergencyCareEpisodeReasonRepository.findByEpisodeId(episodeId)
                .stream()
                .map(ReasonBo::new)
                .collect(Collectors.toList());
        result.setReasons(reasons);

        result.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, result.getCreatedOn()));
		LOG.debug(OUTPUT, result);
		return result;
    }

    @Override
    public Boolean validateAndSetPatient(Integer episodeId, Integer patientId, Integer institutionId) {
        LOG.debug("Input parameters -> episodeId {}, patientId {}",
                episodeId, patientId);
        assertPatientValid(patientId, institutionId);
        emergencyCareEpisodeRepository.updatePatientId(episodeId, patientId);
        return true;
    }

    @Override
    public EmergencyCareBo createAdministrative(EmergencyCareBo newEmergencyCare) {
        return createEpisode(newEmergencyCare,administrativeTriageSaveFunction());
    }

    @Override
    public EmergencyCareBo createAdult(EmergencyCareBo newEmergencyCare) {
        return createEpisode(newEmergencyCare, adultTriageSaveFunction());
    }

    @Override
    public EmergencyCareBo createPediatric(EmergencyCareBo newEmergencyCare) {
        return createEpisode(newEmergencyCare, pediatricTriageSaveFunction());
    }

    private void validPatient(PatientECEBo patient, Integer institutionId) {
        if (patient != null)
            assertPatientValid(patient.getId(), institutionId);
    }

    private void assertPatientValid(Integer patientId, Integer institutionId) {
        boolean violatesConstraint = emergencyCareEpisodeRepository.existsActiveEpisodeByPatientIdAndInstitutionId(patientId,institutionId);
        if (violatesConstraint)
            throw new ValidationException("care-episode.patient.invalid");
    }

    private EmergencyCareBo createEpisode(EmergencyCareBo newEmergencyCare, BiFunction<TriageBo,Integer, TriageBo> saveTriage) {

        LOG.debug("Input parameters -> newEmergencyCare {}", newEmergencyCare);
        PoliceInterventionBo policeInterventionBo = newEmergencyCare.getPoliceIntervention();
        policeInterventionBo = (policeInterventionBo != null) ? savePoliceIntervention(newEmergencyCare.getPoliceIntervention()) : new PoliceInterventionBo();
        EmergencyCareBo emergencyCareEpisodeBo = saveEmergencyCareEpisode(newEmergencyCare, newEmergencyCare.getTriage(), policeInterventionBo.getId());

        TriageBo triageBo = saveTriage.apply(newEmergencyCare.getTriage(), emergencyCareEpisodeBo.getId());

        List<ReasonBo> reasons = saveReasons(newEmergencyCare.getReasons(), emergencyCareEpisodeBo.getId());

        emergencyCareEpisodeBo.setPoliceIntervention(policeInterventionBo);
        emergencyCareEpisodeBo.setTriage(triageBo);
        emergencyCareEpisodeBo.setReasons(reasons);

        LOG.debug(OUTPUT, emergencyCareEpisodeBo);
        return emergencyCareEpisodeBo;
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
        validPatient(emergencyCareBo.getPatient(), emergencyCareBo.getInstitutionId());
        EmergencyCareEpisode emergencyCareEpisode = new EmergencyCareEpisode(emergencyCareBo, triageBo, policeInterventionId);
        emergencyCareEpisode = emergencyCareEpisodeRepository.save(emergencyCareEpisode);
        EmergencyCareBo result = new EmergencyCareBo(emergencyCareEpisode);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private BiFunction<TriageBo,Integer, TriageBo> pediatricTriageSaveFunction () {
        return this::saveTriagePediatric;
    }

    private BiFunction<TriageBo,Integer, TriageBo> adultTriageSaveFunction () {
        return this::saveTriageAdult;
    }

    private BiFunction<TriageBo,Integer, TriageBo> administrativeTriageSaveFunction () {
        return this::saveTriageAdministrative;
    }

    private TriageBo saveTriageAdult(TriageBo triageBo, Integer emergencyCareEpisodeId) {
        LOG.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}", triageBo, emergencyCareEpisodeId);
        triageBo.setEmergencyCareEpisodeId(emergencyCareEpisodeId);
        TriageBo result = triageService.createAdultGynecological(triageBo);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private TriageBo saveTriageAdministrative(TriageBo triageBo, Integer emergencyCareEpisodeId) {
        LOG.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}", triageBo, emergencyCareEpisodeId);
        triageBo.setEmergencyCareEpisodeId(emergencyCareEpisodeId);
        TriageBo result = triageService.createAdministrative(triageBo);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private TriageBo saveTriagePediatric(TriageBo triageBo, Integer emergencyCareEpisodeId) {
        LOG.debug("Input parameters -> triageBo {}, emergencyCareEpisodeId {}", triageBo, emergencyCareEpisodeId);
        triageBo.setEmergencyCareEpisodeId(emergencyCareEpisodeId);
        TriageBo result = triageService.createPediatric(triageBo);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private List<ReasonBo> saveReasons(List<ReasonBo> reasons, Integer emergencyCareEpisodeId) {
        LOG.debug("Input parameters -> reasons {}, emergencyCareEpisodeId {}", reasons, emergencyCareEpisodeId);
        reasons.forEach(reason -> {
            EmergencyCareEpisodeReason emergencyCareEpisodeReason = new EmergencyCareEpisodeReason(emergencyCareEpisodeId, reason.getId());
            emergencyCareEpisodeReasonRepository.save(emergencyCareEpisodeReason);
        });
        LOG.debug(OUTPUT, reasons);
        return reasons;
    }

    private LocalDateTime UTCIntoInstitutionLocalDateTime(Integer institutionId, LocalDateTime date) {
        LOG.debug("Input parameters -> institutionId {}, date {}", institutionId, date);
        ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
        LocalDateTime result = date
                .atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
                .withZoneSameInstant(institutionZoneId)
                .toLocalDateTime();
        LOG.debug(OUTPUT, result);
        return result;
    }
}
