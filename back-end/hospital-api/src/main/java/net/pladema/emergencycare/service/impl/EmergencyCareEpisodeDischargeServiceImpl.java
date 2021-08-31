package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import io.jsonwebtoken.lang.Assert;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.emergencycare.controller.EmergencyCareEpisodeMedicalDischargeController;
import net.pladema.emergencycare.repository.DischargeTypeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeDischargeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.domain.EpisodeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmergencyCareEpisodeDischargeServiceImpl implements EmergencyCareEpisodeDischargeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeMedicalDischargeController.class);

    private final EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository;
    private final DocumentFactory documentFactory;
    private final DischargeTypeRepository dischargeTypeRepository;
    private final DocumentService documentService;
    private final DocumentHealthConditionRepository documentHealthConditionRepository;
    private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;
    private final DateTimeProvider dateTimeProvider;

    EmergencyCareEpisodeDischargeServiceImpl(EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository, DocumentFactory documentFactory,
                                             DischargeTypeRepository dischargeTypeRepository, DocumentService documentService, DocumentHealthConditionRepository documentHealthConditionRepository, EmergencyCareEpisodeRepository emergencyCareEpisodeRepository, DateTimeProvider dateTimeProvider) {
        this.emergencyCareEpisodeDischargeRepository = emergencyCareEpisodeDischargeRepository;
        this.documentFactory = documentFactory;
        this.dischargeTypeRepository = dischargeTypeRepository;
        this.documentService = documentService;
        this.documentHealthConditionRepository = documentHealthConditionRepository;
        this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public boolean newMedicalDischarge(MedicalDischargeBo medicalDischarge, ZoneId institutionZoneId, Integer institutionId) {
        LOG.debug("Medical discharge service -> medicalDischargeBo {}", medicalDischarge);
        validateMedicalDischarge(medicalDischarge, institutionZoneId, institutionId);
        EmergencyCareDischarge newDischarge = toEmergencyCareDischarge(medicalDischarge);
        emergencyCareEpisodeDischargeRepository.save(newDischarge);
        documentFactory.run(medicalDischarge, false);
        return true;
    }

    @Override
    public EpisodeDischargeBo getDischarge(Integer episodeId) {
        LOG.debug("Get discharge -> episodeId {}", episodeId);
        EmergencyCareDischarge emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.findById(episodeId)
                .orElseThrow(()->new NotFoundException("episode-discharge-not-found", "Episode discharge not found"));
        DischargeType dischargeType = dischargeTypeRepository.findById(emergencyCareDischarge.getDischargeTypeId())
                .orElseThrow(()->new NotFoundException("discharge-type-not-found", "Discharge type not found"));
        EpisodeDischargeBo episodeDischargeBo = new EpisodeDischargeBo(emergencyCareDischarge, dischargeType);
        Long documentId = documentService.getDocumentId(emergencyCareDischarge.getEmergencyCareEpisodeId(), SourceType.EMERGENCY_CARE).get(0);
        List<HealthConditionVo> resultQuery = documentHealthConditionRepository.getHealthConditionFromDocument(documentId);
        List<SnomedBo> problems = resultQuery.stream().map( r -> new SnomedBo(r.getSnomed())).collect(Collectors.toList());
        episodeDischargeBo.setProblems(problems);
        LOG.debug("output -> episodeDischargeBo {}", episodeDischargeBo);
        return episodeDischargeBo;
    }

    private EmergencyCareDischarge toEmergencyCareDischarge(MedicalDischargeBo medicalDischarge ) {
        return new EmergencyCareDischarge(medicalDischarge.getSourceId(),medicalDischarge.getMedicalDischargeOn(),medicalDischarge.getMedicalDischargeBy(),medicalDischarge.getAutopsy(), medicalDischarge.getDischargeTypeId());
    }

    private void validateMedicalDischarge(MedicalDischargeBo medicalDischarge, ZoneId institutionZoneId, Integer institutionId) {
        EmergencyCareVo emergencyCareVo = assertExistEmergencyCareEpisode(medicalDischarge.getSourceId(),institutionId);
        //TODO validar que el episodio este en atencion
        LocalDateTime medicalDischargeOn = medicalDischarge.getMedicalDischargeOn();
        assertMedicalDischargeIsAfterEpisodeCreationDate(medicalDischargeOn, emergencyCareVo.getCreatedOn(), institutionZoneId);
        assertMedicalDischargeIsBeforeToday(institutionZoneId, medicalDischargeOn);
    }

    private EmergencyCareVo assertExistEmergencyCareEpisode(Integer episodeId, Integer institutionId) {
        return emergencyCareEpisodeRepository.getEpisode(episodeId, institutionId)
                .orElseThrow(() -> new NotFoundException("El episodio de guardia no existe", "El episodio de guardia no existe"));
    }

    private void assertMedicalDischargeIsAfterEpisodeCreationDate(LocalDateTime medicalDischargeOn, LocalDateTime emergencyCareCreatedOn, ZoneId institutionZoneId) {
        LocalDateTime zonedEmergencyCareCreatedOn = emergencyCareCreatedOn
                .atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
                .withZoneSameInstant(institutionZoneId)
                .toLocalDateTime()
                .truncatedTo(ChronoUnit.MINUTES);
        Assert.isTrue( !medicalDischargeOn.isBefore(zonedEmergencyCareCreatedOn), "care-episode.medical-discharge.exceeds-min-date");
    }

    private void assertMedicalDischargeIsBeforeToday(ZoneId institutionZoneId, LocalDateTime medicalDischargeOn) {
        LocalDateTime today = dateTimeProvider.nowDateTimeWithZone(institutionZoneId);
        Assert.isTrue( !medicalDischargeOn.isAfter(today), "care-episode.medical-discharge.exceeds-max-date");
    }
}