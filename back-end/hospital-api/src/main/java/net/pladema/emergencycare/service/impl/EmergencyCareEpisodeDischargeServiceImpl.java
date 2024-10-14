package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthConditionRepository;
import io.jsonwebtoken.lang.Assert;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeDischargeOtherTypeDescriptionStorage;
import net.pladema.emergencycare.infrastructure.output.entity.EmergencyCareEpisodeDischargeOtherTypeDescription;
import net.pladema.emergencycare.repository.DischargeTypeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeDischargeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EpisodeDischargeBo;
import net.pladema.emergencycare.service.domain.EpisodeDischargeSummaryBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.staff.application.ports.HealthcareProfessionalStorage;

import net.pladema.staff.domain.ProfessionalCompleteBo;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmergencyCareEpisodeDischargeServiceImpl implements EmergencyCareEpisodeDischargeService {

    private final EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository;

    private final DocumentFactory documentFactory;

    private final DischargeTypeRepository dischargeTypeRepository;

    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

    private final DateTimeProvider dateTimeProvider;

	private final EmergencyCareEpisodeService emergencyCareEpisodeService;
	private final EmergencyCareEpisodeDischargeOtherTypeDescriptionStorage ecedotDescriptionStorage;
	private final HealthcareProfessionalStorage healthcareProfessionalStorage;

    @Override
    public boolean newMedicalDischarge(MedicalDischargeBo medicalDischarge,Integer institutionId) {
        log.debug("Medical discharge service -> medicalDischargeBo {}", medicalDischarge);
        validateMedicalDischarge(medicalDischarge, institutionId);
        EmergencyCareDischarge newDischarge = toEmergencyCareDischarge(medicalDischarge);
		newDischarge = emergencyCareEpisodeDischargeRepository.save(newDischarge);
		if (newDischarge.getDischargeTypeId().equals(DischargeType.OTRO))
			ecedotDescriptionStorage.save(
					new EmergencyCareEpisodeDischargeOtherTypeDescription(newDischarge.getEmergencyCareEpisodeId(),medicalDischarge.getOtherDischargeDescription())
			);
        newDischarge.setDocumentId(documentFactory.run(medicalDischarge, false));
		emergencyCareEpisodeDischargeRepository.save(newDischarge);
        return true;
    }

    @Override
    public EpisodeDischargeBo getDischarge(Integer episodeId) {
        log.debug("Get discharge -> episodeId {}", episodeId);
        EmergencyCareDischarge emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.findById(episodeId)
                .orElseThrow(()->new NotFoundException("episode-discharge-not-found", "Episode discharge not found"));
        DischargeType dischargeType = dischargeTypeRepository.findById(emergencyCareDischarge.getDischargeTypeId())
                .orElseThrow(()->new NotFoundException("discharge-type-not-found", "Discharge type not found"));
		ProfessionalCompleteBo professionalCompleteBo = healthcareProfessionalStorage.fetchProfessionalById(
				emergencyCareDischarge.getMedicalDischargeByProfessional());
        EpisodeDischargeBo episodeDischargeBo = new EpisodeDischargeBo(
				emergencyCareDischarge,
				dischargeType,
				professionalCompleteBo.getFirstName(),
				professionalCompleteBo.getLastName(),
				getOtherDischargeDescription(emergencyCareDischarge.getEmergencyCareEpisodeId(), emergencyCareDischarge.getDischargeTypeId()),
				emergencyCareDischarge.getObservation()
		);
        List<HealthConditionVo> resultQuery = documentHealthConditionRepository.getHealthConditionFromDocument(emergencyCareDischarge.getDocumentId());
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

	@Override
	public EpisodeDischargeSummaryBo getEpisodeDischargeSummary(Integer episodeId){
		log.debug("Get discharge summary -> episodeId {}", episodeId);
		EmergencyCareDischarge emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.findById(episodeId)
				.orElseThrow(()->new NotFoundException("episode-discharge-not-found", "Episode discharge not found"));
		ProfessionalCompleteBo professionalCompleteBo = healthcareProfessionalStorage.fetchProfessionalById(
				emergencyCareDischarge.getMedicalDischargeByProfessional());
		EpisodeDischargeSummaryBo episodeDischargeSummaryBo = new EpisodeDischargeSummaryBo(
				emergencyCareDischarge,
				professionalCompleteBo.getFirstName(),
				professionalCompleteBo.getLastName(),
				professionalCompleteBo.getNameSelfDetermination()
		);
		log.debug("output -> episodeDischargeSummaryBo {}", episodeDischargeSummaryBo);
		return episodeDischargeSummaryBo;
	}

	private EmergencyCareDischarge toEmergencyCareDischarge(MedicalDischargeBo medicalDischarge ) {
        return new EmergencyCareDischarge(medicalDischarge.getSourceId(),medicalDischarge.getMedicalDischargeOn(),
				medicalDischarge.getMedicalDischargeBy(),medicalDischarge.getAutopsy(),
				medicalDischarge.getDischargeTypeId(), medicalDischarge.getObservation());
    }

    private void validateMedicalDischarge(MedicalDischargeBo medicalDischarge, Integer institutionId) {
        EmergencyCareVo emergencyCareVo = assertExistEmergencyCareEpisode(medicalDischarge.getSourceId(),institutionId);
        //TODO validar que el episodio este en atencion
        LocalDateTime medicalDischargeOn = medicalDischarge.getMedicalDischargeOn();
        assertNotAlreadyDischarged(emergencyCareVo.getId());
        assertMedicalDischargeIsAfterEpisodeCreationDate(medicalDischargeOn, emergencyCareVo.getCreatedOn());
        assertMedicalDischargeIsBeforeToday(medicalDischargeOn);
		assertHasEvolutionNote(medicalDischarge.getEncounterId());
		assertDischargeTypeAndDescription(medicalDischarge.getDischargeTypeId(), medicalDischarge.getOtherDischargeDescription());
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

	private void assertDischargeTypeAndDescription(Short dischargeTypeId, String otherDischargeDescription){
		Assert.isTrue((!Objects.equals(dischargeTypeId, DischargeType.OTRO) && otherDischargeDescription == null) ||
						(Objects.equals(dischargeTypeId, DischargeType.OTRO) && otherDischargeDescription != null)
				,"El episodio debe contar con una descripción del tipo de egreso 'Otro' para iniciar el alta médica.");
	}

	private String getOtherDischargeDescription(Integer emergencyCareEpisodeId, Short dischargeTypeId){
		if (dischargeTypeId.equals(DischargeType.OTRO))
			return ecedotDescriptionStorage.getByEmergencyCareEpisodeId(emergencyCareEpisodeId)
					.map(EmergencyCareEpisodeDischargeOtherTypeDescription::getDescription)
					.orElse(null);
		return null;
	}

}