package net.pladema.medicalconsultation.diary.controller.mapper;

import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableProtectedAppointmentsBo;
import net.pladema.medicalconsultation.diary.controller.dto.CompleteDiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryAvailableProtectedAppointmentsDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryListDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OccupationDto;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;
import java.util.List;

@Mapper(uses = {LocalDateMapper.class, DiaryOpeningHoursMapper.class})
public interface DiaryMapper {

    @Named("toOccupationDto")
    OccupationDto toOccupationDto(OccupationBo occupationBo);

    @Named("toListOccupationDto")
    @IterableMapping(qualifiedByName = "toOccupationDto")
    List<OccupationDto> toListOccupationDto(List<OccupationBo> occupationBos);

    @Named("toDiaryBo")
    @Mapping(target = "diaryOpeningHours", source = "diaryOpeningHours")
    DiaryBo toDiaryBo(DiaryADto diaryADto);

    @Named("toDiaryListDto")
    DiaryListDto toDiaryListDto(DiaryBo diaryBo);

    @Named("toCollectionDiaryListDto")
    @IterableMapping(qualifiedByName = "toDiaryListDto")
    Collection<DiaryListDto> toCollectionDiaryListDto(Collection<DiaryBo> diaryBos);
    
    @Named("toListDiaryOpeningHoursDto")
    Collection<DiaryOpeningHoursDto> toListDiaryOpeningHoursDto(Collection<DiaryOpeningHoursBo> resultService);
    
    @Named("toCompleteDiaryDto")
    @Mapping(target = "diaryOpeningHours", source = "diaryOpeningHours")
    CompleteDiaryDto toCompleteDiaryDto(CompleteDiaryBo completeDiaryBo);

	@Named("toDiaryAvailableProtectedAppointmentsDto")
	DiaryAvailableProtectedAppointmentsDto toDiaryAvailableProtectedAppointmentsDto(DiaryAvailableProtectedAppointmentsBo diaryAvailableProtectedAppointmentsBo);

}
