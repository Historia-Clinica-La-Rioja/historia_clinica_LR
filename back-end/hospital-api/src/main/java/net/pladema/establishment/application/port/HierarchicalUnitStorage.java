package net.pladema.establishment.application.port;

import net.pladema.establishment.service.domain.HierarchicalUnitBo;

import java.util.List;

public interface HierarchicalUnitStorage {

	void deleteHierarchicalUnitStaff(Integer userId);

	List<HierarchicalUnitBo> fetchsHierarchicalUnistByUserIdAndInstitutionId(Integer userId, Integer institutionId);

}
