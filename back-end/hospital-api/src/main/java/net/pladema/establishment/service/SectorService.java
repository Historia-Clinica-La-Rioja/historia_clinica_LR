package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.AttentionPlacesQuantityBo;
import net.pladema.establishment.service.domain.SectorBO;

import java.util.List;

public interface SectorService {

	List<SectorBO> getSectorOfType(Integer institutionId, Short sectorTypeId);

	AttentionPlacesQuantityBo quantityAttentionPlacesBySectorType(Integer institutionId, Short sectorTypeId);

	String getSectorName(Integer sectorId);
}
