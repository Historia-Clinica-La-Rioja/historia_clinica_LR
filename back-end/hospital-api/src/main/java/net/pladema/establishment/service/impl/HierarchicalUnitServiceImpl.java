package net.pladema.establishment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.service.HierarchicalUnitService;

import net.pladema.establishment.service.domain.HierarchicalUnitBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HierarchicalUnitServiceImpl implements HierarchicalUnitService {

	private final HierarchicalUnitRepository hierarchicalUnitRepository;
	@Override
	public List<HierarchicalUnitBo> getByInstitution(Integer institutionId) {
		log.debug("Input institutionId {}", institutionId);
		List<HierarchicalUnitBo> result = hierarchicalUnitRepository.getAllByInstitutionId(institutionId).stream()
				.map(hu -> new HierarchicalUnitBo(hu.getId(), hu.getAlias()))
				.collect(Collectors.toList());

		log.debug("Output {}", result);
		return result;
	}
}
