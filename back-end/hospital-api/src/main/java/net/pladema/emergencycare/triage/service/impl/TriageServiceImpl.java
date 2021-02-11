package net.pladema.emergencycare.triage.service.impl;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.triage.repository.TriageDetailsRepository;
import net.pladema.emergencycare.triage.repository.TriageRepository;
import net.pladema.emergencycare.triage.repository.TriageVitalSignsRepository;
import net.pladema.emergencycare.triage.repository.domain.TriageVo;
import net.pladema.emergencycare.triage.repository.entity.Triage;
import net.pladema.emergencycare.triage.repository.entity.TriageDetails;
import net.pladema.emergencycare.triage.repository.entity.TriageVitalSigns;
import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import net.pladema.emergencycare.triage.service.domain.enums.EBodyTemperature;
import net.pladema.emergencycare.triage.service.domain.enums.EMuscleHypertonia;
import net.pladema.emergencycare.triage.service.domain.enums.EPerfusion;
import net.pladema.emergencycare.triage.service.domain.enums.ERespiratoryRetraction;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class TriageServiceImpl implements TriageService {

    private static final Logger LOG = LoggerFactory.getLogger(TriageServiceImpl.class);

    private final TriageRepository triageRepository;

    private final TriageDetailsRepository triageDetailsRepository;

    private final TriageVitalSignsRepository triageVitalSignsRepository;

    private final InstitutionExternalService institutionExternalService;

    private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

    public TriageServiceImpl(TriageRepository triageRepository,
                             TriageDetailsRepository triageDetailsRepository,
                             TriageVitalSignsRepository triageVitalSignsRepository,
                             InstitutionExternalService institutionExternalService,
                             EmergencyCareEpisodeRepository emergencyCareEpisodeRepository) {
        super();
        this.triageRepository = triageRepository;
        this.triageDetailsRepository = triageDetailsRepository;
        this.triageVitalSignsRepository = triageVitalSignsRepository;
        this.institutionExternalService = institutionExternalService;
        this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
    }

    @Override
    public List<TriageBo> getAll(Integer institutionId, Integer episodeId) {
        LOG.debug("Input parameter -> institutionId {}, episodeId {}", institutionId, episodeId);
        List<TriageVo> triageVos = triageRepository.getAllByEpisodeId(episodeId);
        List<TriageBo> result = triageVos.stream()
                .map(TriageBo::new)
                .collect(Collectors.toList());
        result.forEach(t -> {
            setDetailsDescriptions(t);
            t.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, t.getCreatedOn()));
        });
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    private LocalDateTime UTCIntoInstitutionLocalDateTime(Integer institutionId, LocalDateTime date) {
        LOG.debug("Input parameters -> institutionId {}, date {}", institutionId, date);
        ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
        LocalDateTime result = date
                .atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
                .withZoneSameInstant(institutionZoneId)
                .toLocalDateTime();
        LOG.debug("Output -> {}", result);
        return result;
    }

    private void setDetailsDescriptions(TriageBo triage) {
        LOG.debug("Input parameter -> triage {}", triage);
        if (triage.getBodyTemperatureId() != null)
            triage.setBodyTemperatureDescription(EBodyTemperature.getById(triage.getBodyTemperatureId()).getDescription());
        if (triage.getMuscleHypertoniaId() != null)
            triage.setMuscleHypertoniaDescription(EMuscleHypertonia.getById(triage.getMuscleHypertoniaId()).getDescription());
        if (triage.getRespiratoryRetractionId() != null)
            triage.setRespiratoryRetractionDescription(ERespiratoryRetraction.getById(triage.getRespiratoryRetractionId()).getDescription());
        if (triage.getPerfusionId() != null)
            triage.setPerfusionDescription(EPerfusion.getById(triage.getPerfusionId()).getDescription());
    }

    @Override
    public TriageBo createAdministrative(TriageBo triageBo) {
        return persistTriage(triageBo, getAdministrativeConsumer());
    }

    @Override
    public TriageBo createAdultGynecological(TriageBo triageBo) {
        return persistTriage(triageBo, getAdultConsumer());
    }

    @Override
    public TriageBo createPediatric(TriageBo triageBo) {
        return persistTriage(triageBo, getPediatricConsumer());
    }

    private TriageBo persistTriage(TriageBo triageBo, Consumer<TriageBo> consumer) {
        LOG.debug("Input parameter -> triageBo {}", triageBo);
        Triage triage = triageRepository.save(new Triage(triageBo));
        triageBo.setId(triage.getId());

        consumer.accept(triageBo);

        Integer episodeId = triageBo.getEmergencyCareEpisodeId();
        Short categoryId = triageBo.getCategoryId();
        this.setTriageCategoryId(episodeId, categoryId);

        LOG.debug("Output -> {}", triageBo);
        return triageBo;
    }

    private Consumer<TriageBo> getAdultConsumer() {
        return triageBo -> saveVitalSigns(triageBo.getId(), triageBo.getVitalSignIds());
    }

    private Consumer<TriageBo> getPediatricConsumer() {
        return triageBo -> {
            if (existDetails(triageBo))
                triageDetailsRepository.save(new TriageDetails(triageBo));
            saveVitalSigns(triageBo.getId(), triageBo.getVitalSignIds());
        };
    }

    private Consumer<TriageBo> getAdministrativeConsumer() {
        return triageBo -> {};
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

    private Boolean setTriageCategoryId(Integer episodeId, Short triageCategoryId) {
        LOG.debug("Input parameters -> episodeId {}, triageCategoryId {}",
                episodeId, triageCategoryId);
        emergencyCareEpisodeRepository.updateTriageCategoryId(episodeId, triageCategoryId);
        return true;
    }
}
