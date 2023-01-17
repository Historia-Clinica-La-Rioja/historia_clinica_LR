package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.EquipmentBO;

import java.util.List;

public interface EquipmentService {

	List<EquipmentBO> getEquipmentBySector(Integer sectorId);
}
