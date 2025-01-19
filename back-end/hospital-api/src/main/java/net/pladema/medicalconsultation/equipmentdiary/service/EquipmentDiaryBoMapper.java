package net.pladema.medicalconsultation.equipmentdiary.service;

import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryDto;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentOpeningHoursBo;



import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface EquipmentDiaryBoMapper {

    @Named("toOpeningHours")
    OpeningHours toOpeningHours(EquipmentOpeningHoursBo equipmentOpeningHoursBo);


	@Named("toEquipmentDiaryDto")
	EquipmentDiaryDto toEquipmentDiaryDto(EquipmentDiaryBo equipmentDiaryBo);

	@Named("toListEquipmentDiaryDto")
	List<EquipmentDiaryDto> toListEquipmentDiaryDto(List<EquipmentDiaryBo> equipmentDiariesBo);
}
