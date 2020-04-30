package net.pladema.internation.controller.mapper.ips;

import net.pladema.internation.controller.dto.ips.AnthropometricDataDto;
import net.pladema.internation.service.domain.ips.AnthropometricDataBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface AnthropometricDataMapper {

    @Named("fromAnthropometricDataDto")
    AnthropometricDataBo fromAnthropometricDataDto(AnthropometricDataDto anthropometricDataDto);

    @Named("fromAnthropometricDataBo")
    AnthropometricDataDto fromAnthropometricDataBo(AnthropometricDataBo anthropometricDataBo);

}
