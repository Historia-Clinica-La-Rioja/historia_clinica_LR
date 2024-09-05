package net.pladema.emergencycare.application;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeListStorage;
import net.pladema.emergencycare.application.port.output.HistoricEmergencyEpisodeStorage;
import net.pladema.emergencycare.domain.EmergencyCareEpisodeFilterBo;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.triage.application.fetchlasttriagebyemergencycareepisodeid.FetchLastTriageByEmergencyCareEpisodeId;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class GetAllEpisodeListByFilter {

	private final EmergencyCareEpisodeListStorage emergencyCareEpisodeListStorage;

	private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	private final InstitutionExternalService institutionExternalService;

	private final FetchLastTriageByEmergencyCareEpisodeId fetchLastTriageByEmergencyCareEpisodeId;

	private final HistoricEmergencyEpisodeStorage historicEmergencyEpisodeStorage;

	private final EmergencyCareEpisodeDischargeService emergencyCareEpisodeDischargeService;

	private final FeatureFlagsService featureFlagsService;

	public Page<EmergencyCareBo> run(Integer institutionId, EmergencyCareEpisodeFilterBo filter, Pageable pageable) {
		log.debug("Input parameters -> institutionId {}, filter {}, pageable {}", institutionId, filter, pageable);
		boolean selfPerceivedFF = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);
		Page<EmergencyCareBo> result = emergencyCareEpisodeListStorage.getAllEpisodeListByFilter(institutionId, filter, pageable);
		result.forEach(ec -> {
			ec.setCreatedOn(UTCIntoInstitutionLocalDateTime(institutionId, ec.getCreatedOn()));
			ec.setTriage(fetchLastTriageByEmergencyCareEpisodeId.run(ec.getId()));
			ec.setCanBeAbsent(getCanBeAbsent(ec.getId(), ec.getEmergencyCareStateId()));
			setStateUpdatedOnAndCalls(ec,institutionId);
			if (ec.getEmergencyCareStateId().equals(EEmergencyCareState.ATENCION.getId())) {
				ProfessionalPersonBo professional = new ProfessionalPersonBo(emergencyCareEpisodeRepository.getEmergencyCareEpisodeRelatedProfessionalInfo(ec.getId()));
				ec.setRelatedProfessional(professional);
			}
			if (ec.getEmergencyCareStateId().equals(EEmergencyCareState.ALTA_MEDICA.getId())){
				ec.setDischargeSummary(emergencyCareEpisodeDischargeService.getEpisodeDischargeSummary(ec.getId()));
			}
			if (selfPerceivedFF) setSelfDeterminationNames(ec);
		});
		if (filter.getClinicalSpecialtySectorIds().isEmpty()) {
			log.debug("Output -> {}", result);
			return result;
		}

		var episodeFilters = result.stream()
				.filter(episode -> filter.getClinicalSpecialtySectorIds().contains(episode.getTriage().getClinicalSpecialtySectorId()))
				.collect(Collectors.toList());
		result = new PageImpl<>(episodeFilters, pageable, episodeFilters.size());

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

	private void setSelfDeterminationNames(EmergencyCareBo ec) {
		if (ec.getPatient() != null && ec.getPatient().getPerson() != null && ec.getPatient().getPerson().getNameSelfDetermination() != null){
			ec.getPatient().getPerson().setFirstName(ec.getPatient().getPerson().getNameSelfDetermination());
			ec.getPatient().getPerson().setNameSelfDetermination(null);
		}
		if (ec.getTriage() != null && ec.getTriage().getCreator() != null && ec.getTriage().getCreator().getNameSelfDetermination() != null){
			ec.getTriage().getCreator().setFirstName(ec.getTriage().getCreator().getNameSelfDetermination());
			ec.getTriage().getCreator().setNameSelfDetermination(null);
			ec.getTriage().getCreator().setMiddleNames(null);
		}
		if (ec.getRelatedProfessional() != null && ec.getRelatedProfessional().getNameSelfDetermination() != null){
			ec.getRelatedProfessional().setFirstName(ec.getRelatedProfessional().getNameSelfDetermination());
			ec.getRelatedProfessional().setNameSelfDetermination(null);
			ec.getRelatedProfessional().setMiddleNames(null);
		}
		if (ec.getDischargeSummary() != null && ec.getDischargeSummary().getMedicalDischargeProfessionalNameSelfDetermination() != null){
			ec.getDischargeSummary().setMedicalDischargeProfessionalName(ec.getDischargeSummary().getMedicalDischargeProfessionalNameSelfDetermination());
			ec.getDischargeSummary().setMedicalDischargeProfessionalNameSelfDetermination(null);
		}
	}


	private void setStateUpdatedOnAndCalls(EmergencyCareBo ec, Integer institutionId){
		Optional<HistoricEmergencyEpisodeBo> heeOpt = historicEmergencyEpisodeStorage.getLatestByEpisodeId(ec.getId());
		if (heeOpt.isPresent()) {
			HistoricEmergencyEpisodeBo hee = heeOpt.get();
			ec.setStateUpdatedOn(UTCIntoInstitutionLocalDateTime(institutionId,hee.getChangeStateDate()));
			ec.setCalls(hee.getCalls());
		}
	}
}