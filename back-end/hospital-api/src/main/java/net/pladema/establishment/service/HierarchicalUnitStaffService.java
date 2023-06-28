package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.HierarchicalUnitStaffBo;

import java.util.List;

public interface HierarchicalUnitStaffService {
	List<HierarchicalUnitStaffBo> getByUserId(Integer userId, Integer institutionId);
}
