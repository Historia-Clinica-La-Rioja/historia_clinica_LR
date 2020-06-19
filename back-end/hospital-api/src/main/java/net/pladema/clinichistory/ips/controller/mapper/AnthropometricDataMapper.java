package net.pladema.clinichistory.ips.controller.mapper;

import net.pladema.clinichistory.ips.controller.dto.AnthropometricDataDto;
import net.pladema.clinichistory.ips.service.domain.AnthropometricDataBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface AnthropometricDataMapper {

    @Named("fromAnthropometricDataDto")
    AnthropometricDataBo fromAnthropometricDataDto(AnthropometricDataDto anthropometricDataDto);

    @Named("fromAnthropometricDataBo")
    AnthropometricDataDto fromAnthropometricDataBo(AnthropometricDataBo anthropometricDataBo);

}
