package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.ImmunizationDto;
import net.pladema.clinichistory.documents.service.ips.domain.ImmunizationBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface ImmunizationMapper {

    @Named("toImmunizationDto")
    ImmunizationDto toImmunizationDto(ImmunizationBo immunizationBo);

}
