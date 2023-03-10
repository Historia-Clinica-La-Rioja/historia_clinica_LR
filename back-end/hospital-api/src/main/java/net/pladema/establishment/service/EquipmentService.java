package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.EquipmentBO;

import java.util.List;

public interface EquipmentService {

	List<EquipmentBO> getEquipmentBySector(Integer sectorId);

	List<EquipmentBO> getEquipmentByInstitution(Integer institutionId);

	EquipmentBO getEquipment (Integer equipmentId);

}
