package net.pladema.medicalconsultation.equipmentdiary.service;

import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.OpeningHoursBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface EquipmentDiaryBoMapper {

    @Named("toOpeningHours")
    OpeningHours toOpeningHours(OpeningHoursBo openingHoursBo);
}
