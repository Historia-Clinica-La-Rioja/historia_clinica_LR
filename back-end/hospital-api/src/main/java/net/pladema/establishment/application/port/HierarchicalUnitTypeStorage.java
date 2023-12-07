package net.pladema.establishment.application.port;

import net.pladema.establishment.domain.hierarchicalunits.HierarchicalUnitTypeBo;

import java.util.List;

public interface HierarchicalUnitTypeStorage {

	List<HierarchicalUnitTypeBo> fetchAllByInstitutionId(Integer institutionId);

}
