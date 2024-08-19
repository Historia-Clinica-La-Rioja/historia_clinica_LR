package net.pladema.emergencycare.triage.application.ports;

import net.pladema.emergencycare.triage.domain.TriageReasonBo;

import java.util.List;

public interface TriageReasonStorage {

	List<TriageReasonBo> saveTriageReasons(List<TriageReasonBo> triageReasonsBo);

	List<TriageReasonBo> getAllByTriageId(Integer triageId);
}
