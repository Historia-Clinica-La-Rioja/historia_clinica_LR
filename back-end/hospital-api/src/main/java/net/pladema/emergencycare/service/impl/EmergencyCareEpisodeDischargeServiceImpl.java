package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import io.jsonwebtoken.lang.Assert;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.emergencycare.repository.DischargeTypeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeDischargeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EpisodeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmergencyCareEpisodeDischargeServiceImpl implements EmergencyCareEpisodeDischargeService {

    private final EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository;

    private final DocumentFactory documentFactory;

    private final DischargeTypeRepository dischargeTypeRepository;

    private final DocumentService documentService;

    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

    private final DateTimeProvider dateTimeProvider;

	private final EmergencyCareEpisodeService emergencyCareEpisodeService;

    @Override
    public boolean newMedicalDischarge(MedicalDischargeBo medicalDischarge,Integer institutionId) {
        log.debug("Medical discharge service -> medicalDischargeBo {}", medicalDischarge);
        validateMedicalDischarge(medicalDischarge, institutionId);
        EmergencyCareDischarge newDischarge = toEmergencyCareDischarge(medicalDischarge);
        emergencyCareEpisodeDischargeRepository.save(newDischarge);
        documentFactory.run(medicalDischarge, false);
        return true;
    }

    @Override
    public EpisodeDischargeBo getDischarge(Integer episodeId) {
        log.debug("Get discharge -> episodeId {}", episodeId);
        EmergencyCareDischarge emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.findById(episodeId)
                .orElseThrow(()->new NotFoundException("episode-discharge-not-found", "Episode discharge not found"));
        DischargeType dischargeType = dischargeTypeRepository.findById(emergencyCareDischarge.getDischargeTypeId())
                .orElseThrow(()->new NotFoundException("discharge-type-not-found", "Discharge type not found"));
        EpisodeDischargeBo episodeDischargeBo = new EpisodeDischargeBo(emergencyCareDischarge, dischargeType);
        Long documentId = documentService.getDocumentId(emergencyCareDischarge.getEmergencyCareEpisodeId(), SourceType.EMERGENCY_CARE).get(0);
        List<HealthConditionVo> resultQuery = documentHealthConditionRepository.getHealthConditionFromDocument(documentId);
        List<SnomedBo> problems = resultQuery.stream().map( r -> new SnomedBo(r.getSnomed())).collect(Collectors.toList());
        episodeDischargeBo.setProblems(problems);
        log.debug("output -> episodeDischargeBo {}", episodeDischargeBo);
        return episodeDischargeBo;
    }

	@Override
	public boolean hasMedicalDischarge(Integer episodeId) {
		log.debug("Get discharge -> episodeId {}", episodeId);
		EmergencyCareDischarge emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.findById(episodeId).orElse(null);
		return emergencyCareDischarge != null && emergencyCareDischarge.getMedicalDischargeOn() != null;
	}

	private EmergencyCareDischarge toEmergencyCareDischarge(MedicalDischargeBo medicalDischarge ) {
        return new EmergencyCareDischarge(medicalDischarge.getSourceId(),medicalDischarge.getMedicalDischargeOn(),medicalDischarge.getMedicalDischargeBy(),medicalDischarge.getAutopsy(), medicalDischarge.getDischargeTypeId());
    }

    private void validateMedicalDischarge(MedicalDischargeBo medicalDischarge, Integer institutionId) {
        EmergencyCareVo emergencyCareVo = assertExistEmergencyCareEpisode(medicalDischarge.getSourceId(),institutionId);
        //TODO validar que el episodio este en atencion
        LocalDateTime medicalDischargeOn = medicalDischarge.getMedicalDischargeOn();
        assertNotAlreadyDischarged(emergencyCareVo.getId());
        assertMedicalDischargeIsAfterEpisodeCreationDate(medicalDischargeOn, emergencyCareVo.getCreatedOn());
        assertMedicalDischargeIsBeforeToday(medicalDischargeOn);
		assertHasEvolutionNote(medicalDischarge.getEncounterId());
    }

    private void assertNotAlreadyDischarged(Integer episodeId) {
        Assert.isTrue(!hasMedicalDischarge(episodeId),"El episodio ya fue dado de alta");
    }

    private void assertHasEvolutionNote(Integer emergencyCareEpisodeId) {
		Assert.isTrue(emergencyCareEpisodeService.hasEvolutionNote(emergencyCareEpisodeId), "El episodio debe contar con una nota de evolución para iniciar el alta médica");
	}

    private EmergencyCareVo assertExistEmergencyCareEpisode(Integer episodeId, Integer institutionId) {
        return emergencyCareEpisodeRepository.getEpisodeByEpisodeAndInstitutionId(episodeId, institutionId)
                .orElseThrow(() -> new NotFoundException("El episodio de guardia no existe", "El episodio de guardia no existe"));
    }

    private void assertMedicalDischargeIsAfterEpisodeCreationDate(LocalDateTime medicalDischargeOn, LocalDateTime emergencyCareCreatedOn) {
       Assert.isTrue( !medicalDischargeOn.isBefore(emergencyCareCreatedOn), "care-episode.medical-discharge.exceeds-min-date");
    }

    private void assertMedicalDischargeIsBeforeToday(LocalDateTime medicalDischargeOn) {
        LocalDateTime today = dateTimeProvider.nowDateTime();
        Assert.isTrue( !medicalDischargeOn.isAfter(today), "care-episode.medical-discharge.exceeds-max-date");
    }
}