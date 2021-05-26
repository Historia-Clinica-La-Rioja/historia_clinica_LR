package ar.lamansys.odontology.infrastructure.controller.mapper;

import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.infrastructure.controller.dto.ToothDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = OdontologySnomedMapper.class)
public interface ToothMapper {

    @Named("parseToToothDto")
    @Mapping(source="toothCode", target="code")
    @Mapping(source="snomed", target="snomed", qualifiedByName = "parseToOdontologySnomedDto")
    ToothDto parseToToothDto(ToothBo toothBo);

    @Named("parseToToothDtoList")
    @IterableMapping(qualifiedByName = "parseToToothDto")
    List<ToothDto> parseToToothDtoList(List<ToothBo> toothBoList);
}
