package net.pladema.establishment.infrastructure.port;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.HierarchicalUnitStorage;

import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.repository.HierarchicalUnitStaffRepository;

import net.pladema.establishment.service.domain.HierarchicalUnitBo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class HierarchicalUnitStorgeImpl implements HierarchicalUnitStorage {

	private final HierarchicalUnitRepository hierarchicalUnitRepository;
	
	private final HierarchicalUnitStaffRepository hierarchicalUnitStaffRepository;

	@Override
	public void deleteHierarchicalUnitStaff(Integer userId) {
		hierarchicalUnitStaffRepository.deleteHierarchicalUnitStaffByUserId(userId);
	}

	@Override
	public List<HierarchicalUnitBo> fetchHierarchicalUnitsByUserIdAndInstitutionId(Integer userId, Integer institutionId) {
		log.debug("Fetch all hierarchical units by userId and institutionId");
		return hierarchicalUnitRepository.getAllByUserIdAndInstitutionId(userId, institutionId);
	}

	@Override
	public List<Integer> fetchAllDescendantIdsByHierarchicalUnitId(Integer hierarchicalUnitId) {
		log.debug("Fetch all hierarchical units descendants by hierarchical unit id {} ", hierarchicalUnitId);
		List<Integer> allDescendantIds = new ArrayList<>();
		List<Integer> toProcess = new ArrayList<>();
		toProcess.add(hierarchicalUnitId);
		while (!toProcess.isEmpty())
			toProcess = processDescendants(allDescendantIds, toProcess);
		return allDescendantIds;
	}

	private List<Integer> processDescendants(List<Integer> allDescendantIds, List<Integer> toProcess) {
		allDescendantIds.addAll(toProcess);
		toProcess = hierarchicalUnitRepository.getAllDescendantIdsByHierarchicalUnitIds(toProcess)
				.stream()
				.filter(descendantId -> !allDescendantIds.contains(descendantId))
				.collect(Collectors.toList());
		return toProcess;
	}

}
