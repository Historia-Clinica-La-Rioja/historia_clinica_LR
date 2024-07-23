package net.pladema.emergencycare.application;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeListStorage;
import net.pladema.emergencycare.application.port.output.HistoricEmergencyEpisodeStorage;
import net.pladema.emergencycare.domain.EmergencyCareEpisodeFilterBo;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.triage.application.fetchlasttriagebyemergencycareepisodeid.FetchLastTriageByEmergencyCareEpisodeId;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@AllArgsConstructor
@Service
public class GetAllEpisodeListByFilter {

	private EmergencyCareEpisodeListStorage emergencyCareEpisodeListStorage;

	private EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	private InstitutionExternalService institutionExternalService;
	private FetchLastTriageByEmergencyCareEpisodeId fetchLastTriageByEmergencyCareEpisodeId;
	private final HistoricEmergencyEpisodeStorage historicEmergencyEpisodeStorage;

	public Page<EmergencyCareBo> run(Integer institutionId, EmergencyCareEpisodeFilterBo filter, Pageable pageable) {
		log.debug("Input parameters -> institutionId {}, filter {}, pageable {}", institutionId, filter, pageable);
		Page<EmergencyCareBo> result = emergencyCareEpisodeListStorage.getAllEpisodeListByFilter(institutionId, filter, pageable);
		result.forEach(ec -> {
			ec.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, ec.getCreatedOn()));
			ec.setTriage(fetchLastTriageByEmergencyCareEpisodeId.run(ec.getId()));
			ec.setCanBeAbsent(getCanBeAbsent(ec.getId(), ec.getEmergencyCareStateId()));
			ec.setStateUpdatedOn(historicEmergencyEpisodeStorage.getLatestByEmergencyCareEpisodeId(ec.getId()));
			if (ec.getEmergencyCareStateId().equals(EEmergencyCareState.ATENCION.getId())) {
				ProfessionalPersonBo professional = new ProfessionalPersonBo(emergencyCareEpisodeRepository.getEmergencyCareEpisodeRelatedProfessionalInfo(ec.getId()));
				ec.setRelatedProfessional(professional);
			}
		});
		log.debug("Output -> {}", result);
		return result;
	}

	private LocalDateTime UTCIntoInstitutionLocalDateTime(Integer institutionId, LocalDateTime date) {
		ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
		return date.atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID)).withZoneSameInstant(institutionZoneId).toLocalDateTime();
	}

	private Boolean getCanBeAbsent(Integer episodeId, Short emergencyCareStateId){
		EEmergencyCareState fromState = EEmergencyCareState.getById(emergencyCareStateId);
		return EEmergencyCareState.validTransition(fromState ,EEmergencyCareState.AUSENTE) &&
				!emergencyCareEpisodeRepository.episodeHasEvolutionNote(episodeId);
	}
}