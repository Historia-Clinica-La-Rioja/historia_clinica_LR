package ar.lamansys.odontology.infrastructure.controller.mapper;

import ar.lamansys.odontology.domain.OdontogramQuadrantBo;
import ar.lamansys.odontology.domain.TeethGroupBo;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.infrastructure.controller.dto.TeethGroupDto;
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
    OdontogramQuadrantBo parseToQuadrant(OdontogramQuadrantBo odontogramQuadrantBo);

    @Named("parseToList")
    @IterableMapping(qualifiedByName = "parseTo")
    List<OdontogramQuadrantBo> parseToQuadrantList(List<OdontogramQuadrantBo> odontogramQuadrantBoList);

    @Named("parseTo")
    TeethGroupDto parseToTeethGroup(TeethGroupBo teethGroupBo);

    @Named("parseToList")
    @IterableMapping(qualifiedByName = "parseTo")
    List<TeethGroupDto> parseToTeethGroupList(List<TeethGroupBo> teethGroupBoList);



}
