package net.pladema.establishment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.repository.HierarchicalUnitStaffRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnit;
import net.pladema.establishment.service.HierarchicalUnitStaffService;

import net.pladema.establishment.service.domain.HierarchicalUnitStaffBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class HierarchicalUnitStaffServiceImpl implements HierarchicalUnitStaffService {

	private final HierarchicalUnitStaffRepository hierarchicalUnitStaffRepository;
	private final HierarchicalUnitRepository hierarchicalUnitRepository;

	@Override
	public List<HierarchicalUnitStaffBo> getByUserId(Integer userId, Integer institutionId) {
		log.debug("Input userId {}, institutionId {} ", userId, institutionId);
		List<HierarchicalUnitStaffBo> result = hierarchicalUnitStaffRepository.findByUserIdAndInstitutionId(userId, institutionId).stream()
				.map( hus -> {
					HierarchicalUnit hu = hierarchicalUnitRepository.findById(hus.getHierarchicalUnitId()).get();
					return new HierarchicalUnitStaffBo(hus.getId(), hus.isResponsible(), hu.getId(), hu.getAlias());
		}).collect(Collectors.toList());
		log.debug("Output {} ", result);
		return result;
	}
}
