package net.pladema.establishment.service;


import net.pladema.establishment.controller.dto.EquipmentDto;
import net.pladema.establishment.service.domain.EquipmentBO;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface EquipmentBOMapper {

	@Named("toEquipmentDto")
	EquipmentDto toEquipmentDto(EquipmentBO equipmentBo);
	@Named("toListEquipmentDto")
	List<EquipmentDto> toListEquipmentDto(List<EquipmentBO> equipmentsBo);
}
