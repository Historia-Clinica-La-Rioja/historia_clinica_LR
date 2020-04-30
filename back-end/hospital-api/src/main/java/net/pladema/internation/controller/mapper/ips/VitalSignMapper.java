package net.pladema.internation.controller.mapper.ips;

import net.pladema.internation.controller.dto.ips.VitalSignDto;
import net.pladema.internation.service.domain.ips.VitalSignBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface VitalSignMapper {

    @Named("fromVitalSignDto")
    VitalSignBo fromVitalSignDto(VitalSignDto vitalSignDto);

    @Named("fromVitalSignBo")
    VitalSignDto fromVitalSignBo(VitalSignBo vitalSignBo);

}
