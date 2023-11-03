package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.HierarchicalUnitBo;

import java.util.List;

public interface HierarchicalUnitService {

	List<HierarchicalUnitBo> getByInstitution(Integer institutionId);
}
