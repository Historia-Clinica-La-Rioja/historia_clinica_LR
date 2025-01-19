package net.pladema.medicalconsultation.diary.controller.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursFreeTimesDto;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursFreeTimesBo;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface DiaryOpeningHoursMapper {

    @Named("toDiaryOpeningHoursBo")
    @Mapping(target = "overturnCount", source = "overturnCount", defaultValue = "0")
    DiaryOpeningHoursBo toDiaryOpeningHoursBo(DiaryOpeningHoursDto diaryOpeningHoursDto);

    @Named("toListDiaryOpeningHoursBo")
    @IterableMapping(qualifiedByName = "toDiaryOpeningHoursBo")
    List<DiaryOpeningHoursBo> toListDiaryOpeningHoursBo(List<DiaryOpeningHoursDto> diaryOpeningHoursDto);

	@Named("fromDiaryOpeningHoursFreeTimesBo")
	DiaryOpeningHoursFreeTimesDto fromDiaryOpeningHoursFreeTimesBo(DiaryOpeningHoursFreeTimesBo diaryOpeningHoursFreeTimesBo);

	@Named("fromDiaryOpeningHoursFreeTimesBoList")
	@IterableMapping(qualifiedByName = "fromDiaryOpeningHoursFreeTimesBo")
	List<DiaryOpeningHoursFreeTimesDto> fromDiaryOpeningHoursFreeTimesBoList(List<DiaryOpeningHoursFreeTimesBo> diaryOpeningHoursFreeTimesBos);


}
