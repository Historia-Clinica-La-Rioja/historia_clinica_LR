package net.pladema.emergencycare.triage.application.ports;


import net.pladema.emergencycare.repository.domain.ProfessionalPersonVo;
import net.pladema.emergencycare.triage.domain.TriageBo;

import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TriageStorage {

	Optional<TriageBo> getLatestByEmergencyCareEpisodeId(Integer emergencyCareEpisodeId);

	Optional<ProfessionalPersonBo> getTriageRelatedProfessionalInfo(Integer id);

}
