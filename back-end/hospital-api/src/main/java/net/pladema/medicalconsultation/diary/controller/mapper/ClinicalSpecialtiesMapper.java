package net.pladema.medicalconsultation.diary.controller.mapper;

import java.util.List;

import net.pladema.medicalconsultation.diary.controller.dto.ActiveDiaryClinicalSpecialtyDto;
import net.pladema.medicalconsultation.diary.service.domain.ActiveDiaryClinicalSpecialtyBo;

import org.mapstruct.Mapper;


@Mapper
public interface ClinicalSpecialtiesMapper {
	ActiveDiaryClinicalSpecialtyDto toActiveDiaryClinicalSpecialtyDto(ActiveDiaryClinicalSpecialtyBo activeDiariesAliases);
	List<ActiveDiaryClinicalSpecialtyDto> toActiveDiaryClinicalSpecialtyDto(List<ActiveDiaryClinicalSpecialtyBo> activeDiariesAliases);
}
