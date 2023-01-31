package ar.lamansys.sgh.clinichistory.application.indication.getmostfrequentparenteralplanindicated;

import ar.lamansys.sgh.clinichistory.application.ports.ParenteralPlanStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.ParenteralPlanBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetMostFrequentParenteralPlanIndicated {

	private final ParenteralPlanStorage parenteralPlanStorage;

	public List<ParenteralPlanBo> run(Integer professionalId, Integer institutionId, Integer limit) {
		log.debug("Input parameter -> professionalId {}, institutionId {}, limit {}", professionalId, institutionId, limit);
		List<ParenteralPlanBo> result = parenteralPlanStorage.getMostFrequentParenteralPlans(professionalId, institutionId, limit);
		log.debug("Output -> {}", result);
		return result;
	}
}
