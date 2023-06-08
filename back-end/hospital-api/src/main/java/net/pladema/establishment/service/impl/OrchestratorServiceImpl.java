package net.pladema.establishment.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.OrchestratorRepository;
import net.pladema.establishment.repository.entity.Orchestrator;
import net.pladema.establishment.service.OrchestratorService;
import net.pladema.establishment.service.domain.OrchestratorBO;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrchestratorServiceImpl implements OrchestratorService {

	private final OrchestratorRepository orchestratorRepository;

	public  OrchestratorServiceImpl(OrchestratorRepository orchestratorRepository){
		this.orchestratorRepository = orchestratorRepository;
	}
	@Override
	public OrchestratorBO getOrchestrator(Integer orchestratorId) {
		Orchestrator orchestrator = orchestratorRepository.getById(orchestratorId);
		if (orchestrator != null){
			return createOrchestratorBoInstance(orchestrator);
		}
		return null;
	}

	private OrchestratorBO createOrchestratorBoInstance(Orchestrator orchestrator){

		log.debug("Input parameters -> Orchestrator {}", orchestrator);

		OrchestratorBO orchestratorBO = new OrchestratorBO();
		orchestratorBO.setId(orchestrator.getId());
		orchestratorBO.setName(orchestrator.getName());
		orchestratorBO.setBaseTopic(orchestrator.getBaseTopic());
		orchestratorBO.setSectorId(orchestrator.getSectorId());
		orchestratorBO.setAttempsNumber(orchestrator.getAttempsNumber());
		orchestratorBO.setExecutionStartTime(orchestrator.getExecutionStartTime());
		orchestratorBO.setExecutionEndTime(orchestrator.getExecutionEndTime());
		orchestratorBO.setWeightDays((double)orchestrator.getWeightDays());
		orchestratorBO.setWeightSize((double)orchestrator.getWeightSize());
		orchestratorBO.setWeightPriority((double)orchestrator.getWeightPriority());
		orchestratorBO.setNumberToMove(orchestrator.getNumberToMove());

		log.debug("Output -> OrchestratorBo {}", orchestratorBO);

		return orchestratorBO;
	}
}
