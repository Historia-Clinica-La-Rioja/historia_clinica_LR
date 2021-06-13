package net.pladema.emergencycare.triage.service;

import net.pladema.emergencycare.triage.service.domain.TriageBo;

import java.util.List;

public interface TriageService {

    List<TriageBo> getAll(Integer institutionId, Integer episodeId);

    TriageBo createAdministrative(TriageBo triage, Integer institutionId);

    TriageBo createAdultGynecological(TriageBo triage, Integer institutionId);

    TriageBo createPediatric(TriageBo triage, Integer institutionId);
}


