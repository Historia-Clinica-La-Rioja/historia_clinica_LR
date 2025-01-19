package ar.lamansys.refcounterref.application.updateruleonreferenceregulation;


import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateRuleOnReferenceRegulation {

	private final HistoricReferenceRegulationStorage historicReferenceRegulationStorage;

	public void run(Integer ruleId, Short ruleLevel, List<Integer> ruleIdsToReplace){
		log.debug("Input parameters -> ruleId {}, ruleLevel {}, ruleIdsToReplace {}", ruleId, ruleLevel, ruleIdsToReplace);
		historicReferenceRegulationStorage.updateRuleOnReferences(ruleId, ruleLevel, ruleIdsToReplace);
	}

}
