package net.pladema.medicalconsultation.diary.controller.mapper;

import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.controller.dto.OccupationDto;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, DiaryOpeningHoursMapper.class})
public interface DiaryMapper {

    @Named("toOccupationDto")
    OccupationDto toOccupationDto(OccupationBo occupationBo);

    @Named("toListOccupationDto")
    @IterableMapping(qualifiedByName = "toOccupationDto")
    List<OccupationDto> toListOccupationDto(List<OccupationBo> occupationBos);

    @Named("toDiaryBo")
    @Mapping(target = "diaryOpeningHours", source = "diaryOpeningHours", qualifiedByName = "toListDiaryOpeningHoursBo")
    DiaryBo toDiaryBo(DiaryADto diaryADto);
}
