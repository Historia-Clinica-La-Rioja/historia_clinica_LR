package net.pladema.emergencycare.triage.application.ports;


import net.pladema.emergencycare.triage.domain.TriageBo;

import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import java.util.Optional;

public interface TriageStorage {

	Optional<TriageBo> getLatestByEmergencyCareEpisodeId(Integer emergencyCareEpisodeId);

	Optional<ProfessionalPersonBo> getTriageRelatedProfessionalInfo(Integer id);

}
