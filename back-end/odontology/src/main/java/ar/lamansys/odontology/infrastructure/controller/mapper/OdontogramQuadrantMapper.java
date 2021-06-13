package ar.lamansys.odontology.infrastructure.controller.mapper;

import ar.lamansys.odontology.domain.OdontogramQuadrantBo;
import ar.lamansys.odontology.infrastructure.controller.dto.OdontogramQuadrantDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = ToothMapper.class)
public interface OdontogramQuadrantMapper {

    @Named("parseToOdontongramQuadrantDto")
    @Mapping(source="teeth", target="teeth", qualifiedByName = "parseToToothDtoList")
    OdontogramQuadrantDto parseToOdontongramQuadrantDto(OdontogramQuadrantBo teethGroupBo);

    @Named("parseToOdontongramQuadrantDtoList")
    @IterableMapping(qualifiedByName = "parseToOdontongramQuadrantDto")
    List<OdontogramQuadrantDto> parseToOdontongramQuadrantDtoList(List<OdontogramQuadrantBo> teethGroupBoList);
}
