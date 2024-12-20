package ar.lamansys.refcounterref.application.approvereferencesbyruleid;

import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditReferencesByRuleId {

	private final HistoricReferenceRegulationStorage historicReferenceRegulationStorage;

	@Transactional
	public void run(Integer ruleId, List<Integer> institutionIds){
		log.debug("Input parameters -> ruleId {}", ruleId);
		historicReferenceRegulationStorage.auditReferencesByRuleId(ruleId, institutionIds);
	}

}
