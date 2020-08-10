package net.pladema.medicalconsultation.diary.controller.mapper;

import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface DiaryOpeningHoursMapper {

    @Named("toDiaryOpeningHoursBo")
    @Mapping(target = "overturnCount", source = "overturnCount", defaultValue = "0")
    DiaryOpeningHoursBo toDiaryOpeningHoursBo(DiaryOpeningHoursDto diaryOpeningHoursDto);

    @Named("toListDiaryOpeningHoursBo")
    @IterableMapping(qualifiedByName = "toDiaryOpeningHoursBo")
    List<DiaryOpeningHoursBo> toListDiaryOpeningHoursBo(List<DiaryOpeningHoursDto> diaryOpeningHoursDto);


}
