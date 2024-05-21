package net.pladema.emergencycare.triage.application.ports;

import net.pladema.emergencycare.triage.infrastructure.output.entity.TriageReason;

public interface TriageReasonStorage {

	TriageReason saveTriageReason(TriageReason triageReason);
}
