package net.pladema.medicalconsultation.diary.controller.mapper;

import net.pladema.medicalconsultation.diary.controller.dto.OccupationDto;
import net.pladema.medicalconsultation.diary.service.domain.OccupationBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface DiaryMapper {

    @Named("toOccupationDto")
    OccupationDto toOccupationDto(OccupationBo occupationBo);

    @Named("toListOccupationDto")
    @IterableMapping(qualifiedByName = "toOccupationDto")
    List<OccupationDto> toListOccupationDto(List<OccupationBo> occupationBos);
}
