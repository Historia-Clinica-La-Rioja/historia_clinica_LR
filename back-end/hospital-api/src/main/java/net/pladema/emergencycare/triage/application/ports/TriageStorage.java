package net.pladema.emergencycare.triage.application.ports;


import net.pladema.emergencycare.triage.infrastructure.output.entity.Triage;

import java.util.Optional;

public interface TriageStorage {

	Optional<Triage> getLatestByEmergencyCareEpisodeId(Integer emergencyCareEpisodeId);
}
