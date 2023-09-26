package net.pladema.establishment.infrastructure.port;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.carelineproblem.CareLineProblemStorage;
import net.pladema.establishment.repository.CareLineProblemRepository;
import net.pladema.establishment.service.domain.CareLineProblemBo;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CareLineProblemStorageImpl implements CareLineProblemStorage {

	private final CareLineProblemRepository careLineProblemRepository;

	@Override
	public Map<Integer, List<SnomedBo>> fetchBySnomedSctids(List<Integer> careLineIds, List<String> snomedSctids) {
		log.debug("Fetch all by care lines and snomed sctids");
		List<CareLineProblemBo> resultQuery = careLineProblemRepository.getAllByCareLineIdsAndSnomedSctids(careLineIds, snomedSctids);
		Map<Integer, List<SnomedBo>> result = new HashMap<>();
		careLineIds.forEach(cl -> result.put(cl,
						resultQuery
								.stream()
								.filter(clp -> clp.getCareLineId().equals(cl))
								.map(CareLineProblemBo::getProblem)
								.collect(Collectors.toList()))
		);
		return result;
	}
}
