package net.pladema.establishment.infrastructure.port;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.HierarchicalUnitStorage;

import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.repository.HierarchicalUnitStaffRepository;

import net.pladema.establishment.service.domain.HierarchicalUnitBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HierarchicalUnitStorgeImpl implements HierarchicalUnitStorage {

	private final HierarchicalUnitRepository hierarchicalUnitRepository;
	
	private final HierarchicalUnitStaffRepository hierarchicalUnitStaffRepository;

	@Override
	public void deleteHierarchicalUnitStaff(Integer userId) {
		hierarchicalUnitStaffRepository.deleteHierarchicalUnitStaffByUserId(userId);
	}

	@Override
	public List<HierarchicalUnitBo> fetchsHierarchicalUnistByUserIdAndInstitutionId(Integer userId, Integer institutionId) {
		log.debug("Fetch all hierarchical units by userId and institutionId");
		return hierarchicalUnitRepository.getAllByUserIdAndInstitutionId(userId, institutionId);
	}
}
