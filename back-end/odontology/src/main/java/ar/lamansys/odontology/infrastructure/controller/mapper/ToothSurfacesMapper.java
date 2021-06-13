package ar.lamansys.odontology.infrastructure.controller.mapper;

import ar.lamansys.odontology.domain.ToothSurfacesBo;
import ar.lamansys.odontology.infrastructure.controller.dto.ToothSurfacesDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = OdontologySnomedMapper.class)
public interface ToothSurfacesMapper {

    @Named("parseToToothSurfacesDto")
    ToothSurfacesDto parseToToothSurfacesDto(ToothSurfacesBo toothSurfacesBo);
}
