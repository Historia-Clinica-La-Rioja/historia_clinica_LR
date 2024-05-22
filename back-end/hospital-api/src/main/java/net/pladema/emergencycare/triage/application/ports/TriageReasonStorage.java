package net.pladema.emergencycare.triage.application.ports;

import net.pladema.emergencycare.triage.infrastructure.output.entity.TriageReason;

import java.util.List;

public interface TriageReasonStorage {

	TriageReason saveTriageReason(TriageReason triageReason);

	List<TriageReason> getAllByTriageId(Integer triageId);
}
