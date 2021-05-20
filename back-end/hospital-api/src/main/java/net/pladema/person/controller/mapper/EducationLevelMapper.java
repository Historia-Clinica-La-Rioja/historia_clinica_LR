package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.EducationLevelDto;
import net.pladema.person.service.domain.EducationLevelBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface EducationLevelMapper {

    @Named("fromEducationBo")
    EducationLevelDto fromEducationBo(EducationLevelBo educationLevelBo);

    @Named("fromEducationBoList")
    @IterableMapping(qualifiedByName = "fromEducationBo")
    List<EducationLevelDto> fromEducationBoList(List<EducationLevelBo> educationLevelBoList);
}
