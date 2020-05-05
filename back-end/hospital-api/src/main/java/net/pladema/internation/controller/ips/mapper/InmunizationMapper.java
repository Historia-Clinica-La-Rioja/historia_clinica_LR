package net.pladema.internation.controller.ips.mapper;

import net.pladema.internation.controller.ips.dto.InmunizationDto;
import net.pladema.internation.service.ips.domain.InmunizationBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface InmunizationMapper {

    @Named("toInmunizationDto")
    InmunizationDto toInmunizationDto(InmunizationBo inmunizationBo);

}
