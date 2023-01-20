package ar.lamansys.sgh.clinichistory.application.indication.getmostfrequentpharmacoindicated;

import ar.lamansys.sgh.clinichistory.application.ports.PharmacoStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoSummaryBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetMostFrequentPharmacoIndicated {

	private final PharmacoStorage pharmacoStorage;

	public List<PharmacoSummaryBo> run(Integer professionalId, Integer institutionId, Integer limit) {
		log.debug("Input parameter -> professionalId {}, institutionId {}, limit {}", professionalId, institutionId, limit);
		List<PharmacoSummaryBo> result = pharmacoStorage.getMostFrequentPharmacos(professionalId, institutionId, limit);
		log.debug("Output -> {}", result);
		return result;
	}
}
