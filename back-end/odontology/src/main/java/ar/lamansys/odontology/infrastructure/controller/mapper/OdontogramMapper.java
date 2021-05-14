package ar.lamansys.odontology.infrastructure.controller.mapper;

import ar.lamansys.odontology.domain.OdontogramQuadrantBo;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.infrastructure.controller.dto.OdontogramQuadrantDto;
import ar.lamansys.odontology.infrastructure.controller.dto.ToothDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface OdontogramMapper {

    @Named("parseTo")
    ToothDto parseToTooth(ToothBo toothBo);

    @Named("parseToList")
    @IterableMapping(qualifiedByName = "parseTo")
    List<ToothDto> parseToToothList(List<ToothBo> toothBoList);

    @Named("parseTo")
    OdontogramQuadrantDto parseToOdontongramQuadrant(OdontogramQuadrantBo teethGroupBo);

    @Named("parseToList")
    @IterableMapping(qualifiedByName = "parseTo")
    List<OdontogramQuadrantDto> parseToOdontongramQuadrantList(List<OdontogramQuadrantBo> teethGroupBoList);



}
