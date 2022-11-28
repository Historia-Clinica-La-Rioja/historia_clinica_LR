package net.pladema.vademecum.infraestructure.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.vademecum.application.VademecumStorage;

import net.pladema.vademecum.domain.SnomedBo;


import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VademecumStorageImpl implements VademecumStorage {

	private final SnomedRepository snomedRepository;

	private static final Integer LIMIT = 30;

	@Override
	public List<SnomedBo> searchConcepts(String term, String ecl, Integer institutionId) {
		Integer parentId = this.snomedRepository.getIdFromGroup(ecl);

		List<SnomedBo> vademecumConcepts = this.snomedRepository.getVademecumConcepts(term, parentId, institutionId, PageRequest.of(0, LIMIT))
				.stream()
				.map(s -> new SnomedBo(s.getSctid(), s.getPt())).collect(Collectors.toList());
		log.debug("Output{}", vademecumConcepts);
		return vademecumConcepts;
	}

	@Override
	public Long getTotalConcepts(String term, String ecl, Integer institutionId) {
		Integer parentId = this.snomedRepository.getIdFromGroup(ecl);
		return this.snomedRepository.getTotalConcepts(term, parentId, institutionId);
	}
}
