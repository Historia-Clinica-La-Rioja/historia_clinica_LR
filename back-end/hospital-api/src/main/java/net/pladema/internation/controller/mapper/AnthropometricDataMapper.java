package net.pladema.internation.controller.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.dto.ips.AnthropometricDataDto;
import net.pladema.internation.service.domain.ips.AnthropometricDataBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface AnthropometricDataMapper {

    @Named("fromAnthropometricDataDto")
    @Mapping(target = "bloodType.deleted", source = "deleted")
    @Mapping(target = "height.deleted", source = "deleted")
    @Mapping(target = "weight.deleted", source = "deleted")
    AnthropometricDataBo fromAnthropometricDataDto(AnthropometricDataDto anthropometricDataDto);

    @Named("fromListAnthropometricDataDto")
    @IterableMapping(qualifiedByName = "fromAnthropometricDataDto")
    List<AnthropometricDataBo> fromListAnthropometricDataDto(List<AnthropometricDataDto> anthropometricDataDtos);

    @Named("fromAnthropometricDataBo")
    AnthropometricDataDto fromAnthropometricDataBo(AnthropometricDataBo anthropometricDataBo);

    @Named("fromListAnthropometricDataBo")
    @IterableMapping(qualifiedByName = "fromAnthropometricDataBo")
    List<AnthropometricDataDto> fromListAnthropometricDataBo(List<AnthropometricDataBo> anthropometricDataBos);
}
