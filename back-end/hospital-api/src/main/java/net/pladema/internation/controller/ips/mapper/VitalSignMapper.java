package net.pladema.internation.controller.ips.mapper;

import net.pladema.internation.controller.ips.dto.VitalSignDto;
import net.pladema.internation.service.ips.domain.VitalSignBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface VitalSignMapper {

    @Named("fromVitalSignDto")
    VitalSignBo fromVitalSignDto(VitalSignDto vitalSignDto);

    @Named("fromVitalSignBo")
    VitalSignDto fromVitalSignBo(VitalSignBo vitalSignBo);

}
