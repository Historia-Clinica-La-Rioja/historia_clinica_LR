package net.pladema.internation.controller.mapper.ips;

import net.pladema.internation.controller.dto.ips.InmunizationDto;
import net.pladema.internation.service.domain.ips.InmunizationBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface InmunizationMapper {

    @Named("toInmunizationDto")
    InmunizationDto toInmunizationDto(InmunizationBo inmunizationBo);

}
