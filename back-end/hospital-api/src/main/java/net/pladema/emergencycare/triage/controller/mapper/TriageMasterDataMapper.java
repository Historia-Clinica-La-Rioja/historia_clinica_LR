package net.pladema.emergencycare.triage.controller.mapper;

import net.pladema.emergencycare.triage.controller.dto.TriageCategoryDto;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface TriageMasterDataMapper {

    List<TriageCategoryDto> toTriageCategoryDtoList(List<TriageCategoryBo> triageCategoryBos);

    @Mapping(target = "color.name", source = "colorName")
    @Mapping(target = "color.code", source = "colorCode")
    TriageCategoryDto toTriageCategoryDto(TriageCategoryBo triageCategoryBo);

}
