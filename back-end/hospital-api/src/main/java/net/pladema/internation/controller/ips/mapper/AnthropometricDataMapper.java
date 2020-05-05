package net.pladema.internation.controller.ips.mapper;

import net.pladema.internation.controller.ips.dto.AnthropometricDataDto;
import net.pladema.internation.service.ips.domain.AnthropometricDataBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface AnthropometricDataMapper {

    @Named("fromAnthropometricDataDto")
    AnthropometricDataBo fromAnthropometricDataDto(AnthropometricDataDto anthropometricDataDto);

    @Named("fromAnthropometricDataBo")
    AnthropometricDataDto fromAnthropometricDataBo(AnthropometricDataBo anthropometricDataBo);

}
