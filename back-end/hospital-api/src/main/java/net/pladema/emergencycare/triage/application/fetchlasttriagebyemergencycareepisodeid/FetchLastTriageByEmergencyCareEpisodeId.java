package net.pladema.emergencycare.triage.application.fetchlasttriagebyemergencycareepisodeid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.triage.application.fetchtriagereasons.FetchTriageReasons;
import net.pladema.emergencycare.triage.application.ports.TriageStorage;

import net.pladema.emergencycare.triage.domain.TriageBo;


import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchLastTriageByEmergencyCareEpisodeId {

	private final TriageStorage triageStorage;
	private final FetchTriageReasons fetchTriageReasons;

	public TriageBo run(Integer emergencyCareEpisodeId){
		log.debug("Input parameters -> emergencyCareEpisodeId {} ", emergencyCareEpisodeId);
		Optional<TriageBo> triageOpt = triageStorage.getLatestByEmergencyCareEpisodeId(emergencyCareEpisodeId);
		TriageBo result = null;
		if (triageOpt.isPresent()){
			result = triageOpt.get();
			result.setReasons(fetchTriageReasons.run(result.getTriageId()));
			result.setCreator(triageStorage.getTriageRelatedProfessionalInfo(result.getTriageId()).orElse(null));
		}
		log.debug("Output -> {}", result);
		return result;
	}

}
