package net.pladema.medicalconsultation.equipmentdiary.controller.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryOpeningHoursMapper;


import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryADto;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, DiaryOpeningHoursMapper.class})
public interface EquipmetDiaryMapper {

	@Named("toEquipmentDiaryBo")
	@Mapping(target = "equipmentDiaryOpeningHours", source = "equipmentDiaryOpeningHours")
	EquipmentDiaryBo toEquipmentDiaryBo(EquipmentDiaryADto equipmentDiaryADto);
}
