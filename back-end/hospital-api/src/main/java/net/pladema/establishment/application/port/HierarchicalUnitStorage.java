package net.pladema.establishment.application.port;

import net.pladema.establishment.service.domain.HierarchicalUnitBo;

import java.util.List;

public interface HierarchicalUnitStorage {

	void deleteHierarchicalUnitStaff(Integer userId);

	List<HierarchicalUnitBo> fetchHierarchicalUnitsByUserIdAndInstitutionId(Integer userId, Integer institutionId);

	List<Integer> fetchAllDescendantIdsByHierarchicalUnitId(Integer hierarchicalUnitId);
}
