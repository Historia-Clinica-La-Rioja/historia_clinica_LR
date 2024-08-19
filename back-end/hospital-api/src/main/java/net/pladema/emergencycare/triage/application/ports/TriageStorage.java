package net.pladema.emergencycare.triage.application.ports;


import net.pladema.emergencycare.triage.domain.TriageBo;

import java.util.Optional;

public interface TriageStorage {

	Optional<TriageBo> getLatestByEmergencyCareEpisodeId(Integer emergencyCareEpisodeId);
}
