package ar.lamansys.odontology.infrastructure.controller.mapper;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface OdontologySnomedMapper {

    @Named("parseToOdontologySnomedDto")
    OdontologySnomedBo parseToOdontologySnomedDto(OdontologySnomedBo odontologySnomedBo);
}
