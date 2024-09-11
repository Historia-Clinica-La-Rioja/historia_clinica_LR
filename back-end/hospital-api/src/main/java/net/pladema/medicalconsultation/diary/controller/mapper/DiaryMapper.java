package net.pladema.medicalconsultation.diary.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import net.pladema.medicalconsultation.diary.domain.UpdateDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableAppointmentsBo;
import net.pladema.medicalconsultation.diary.controller.dto.CompleteDiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryAvailableAppointmentsDto;
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

@Mapper(uses = {LocalDateMapper.class, DiaryOpeningHoursMapper.class, SnomedMapper.class})
public interface DiaryMapper {

    @Named("toOccupationDto")
    OccupationDto toOccupationDto(OccupationBo occupationBo);

    @Named("toListOccupationDto")
    @IterableMapping(qualifiedByName = "toOccupationDto")
    List<OccupationDto> toListOccupationDto(List<OccupationBo> occupationBos);

    @Named("toDiaryBo")
    @Mapping(target = "diaryOpeningHours", source = "diaryOpeningHours")
	@Mapping(target = "diaryLabelBo", source = "diaryLabelDto")
    DiaryBo toDiaryBo(DiaryADto diaryADto);

    @Named("toDiaryBo")
    @Mapping(target = "updateDiaryOpeningHours", source = "diaryOpeningHours")
    @Mapping(target = "diaryOpeningHours", ignore = true)
    @Mapping(target = "diaryLabelBo", source = "diaryLabelDto")
    UpdateDiaryBo toUpdateDiaryBo(DiaryADto diaryADto);

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

	@Named("toDiaryAvailableAppointmentsDto")
	DiaryAvailableAppointmentsDto toDiaryAvailableAppointmentsDto(DiaryAvailableAppointmentsBo diaryAvailableAppointmentsBo);

	@Named("toListDiaryAvailableAppointmentsDto")
	List<DiaryAvailableAppointmentsDto> toListDiaryAvailableAppointmentsDto(List<DiaryAvailableAppointmentsBo> diaryAvailableAppointmentsBoList);

}
