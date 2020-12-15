package net.pladema.emergencycare.triage.service;

import net.pladema.emergencycare.triage.service.domain.TriageBo;

import java.util.List;

public interface TriageService {

    List<TriageBo> getAll(Integer episodeId);

    TriageBo createAdministrative(TriageBo triage);

    TriageBo createAdultGynecological(TriageBo triage);

    TriageBo createPediatric(TriageBo triage);
}


