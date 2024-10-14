package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.OrchestratorBO;

import java.util.List;

public interface OrchestratorService {

	OrchestratorBO getOrchestrator(Integer orchestratorId);
	public List<Integer> getOrchestratorActiveMassiveRetry();
}
