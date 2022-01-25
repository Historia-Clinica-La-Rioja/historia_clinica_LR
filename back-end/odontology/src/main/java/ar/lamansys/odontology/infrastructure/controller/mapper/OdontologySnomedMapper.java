package ar.lamansys.odontology.infrastructure.controller.mapper;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.infrastructure.controller.dto.OdontologySnomedDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface OdontologySnomedMapper {

    @Named("parseToOdontologySnomedDto")
    OdontologySnomedDto parseToOdontologySnomedDto(OdontologySnomedBo odontologySnomedBo);
}
