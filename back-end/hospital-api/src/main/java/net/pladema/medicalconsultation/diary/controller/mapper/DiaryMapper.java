package net.pladema.medicalconsultation.diary.controller.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import net.pladema.medicalconsultation.diary.controller.dto.CompleteDiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryListDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OccupationDto;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;

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

    @Named("toDiaryDto")
    DiaryDto toDiaryDto(DiaryBo diaryBo);

    @Named("toDiaryListDto")
    DiaryListDto toDiaryListDto(DiaryBo diaryBo);

    @Named("toCollectionDiaryListDto")
    @IterableMapping(qualifiedByName = "toDiaryListDto")
    Collection<DiaryListDto> toCollectionDiaryListDto(Collection<DiaryBo> diaryBos);
    
    @Named("toListDiaryOpeningHoursDto")
    Collection<DiaryOpeningHoursDto> toListDiaryOpeningHoursDto(Collection<DiaryOpeningHoursBo> resultService);
    
    @Named("toCompleteDiaryDto")
    @Mapping(target = "diaryOpeningHours", source = "diaryOpeningHours", qualifiedByName = "toListDiaryOpeningHoursBo")
    CompleteDiaryDto toCompleteDiaryDto(CompleteDiaryBo completeDiaryBo);
}
