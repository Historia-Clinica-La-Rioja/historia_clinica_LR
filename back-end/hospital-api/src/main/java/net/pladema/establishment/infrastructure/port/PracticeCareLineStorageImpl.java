package net.pladema.establishment.infrastructure.port;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.PracticeCareLineStorage;

import net.pladema.establishment.repository.CareLineInstitutionPracticeRepository;

import net.pladema.establishment.service.domain.CareLinePracticeBo;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PracticeCareLineStorageImpl implements PracticeCareLineStorage {

	private final CareLineInstitutionPracticeRepository careLineInstitutionPracticeRepository;

	@Override
	public Map<Integer, List<SnomedBo>> fetchAllByCareLineIds(List<Integer> careLineIds) {
		log.debug("Fetch all practices adopted by care lines");
		List<CareLinePracticeBo> resultQuery = careLineInstitutionPracticeRepository.getAllByCareLineIds(careLineIds);
		Map<Integer, List<SnomedBo>> result = new HashMap<>();
		careLineIds.stream()
				.forEach(cl -> result.put(cl,
						resultQuery
								.stream()
								.filter(clp -> clp.getCareLineId().equals(cl))
								.map(CareLinePracticeBo::getPractice)
								.collect(Collectors.toList()))
				);
		return result;
	}

}
