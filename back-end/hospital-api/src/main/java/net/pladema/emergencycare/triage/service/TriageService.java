package net.pladema.emergencycare.triage.service;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import net.pladema.emergencycare.triage.domain.TriageBo;

import java.util.List;

public interface TriageService {

    List<TriageBo> getAll(Integer institutionId, Integer episodeId);

    TriageBo createAdministrative(TriageBo triage, Integer institutionId);

    TriageBo createAdultGynecological(TriageBo triage, Integer institutionId);

    TriageBo createPediatric(TriageBo triage, Integer institutionId);

	void addTriageReasons(List<ReasonBo> reasons, Integer triageId);
}


