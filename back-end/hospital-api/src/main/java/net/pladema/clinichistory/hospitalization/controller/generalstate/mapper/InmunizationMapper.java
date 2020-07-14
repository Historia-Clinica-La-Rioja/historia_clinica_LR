package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.InmunizationDto;
import net.pladema.clinichistory.ips.service.domain.InmunizationBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface InmunizationMapper {

    @Named("toInmunizationDto")
    InmunizationDto toInmunizationDto(InmunizationBo inmunizationBo);

}
