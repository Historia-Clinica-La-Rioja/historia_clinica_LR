package net.pladema.internation.controller.mapper.ips;

import net.pladema.internation.controller.dto.ips.AnthropometricDataDto;
import net.pladema.internation.service.domain.ips.AnthropometricDataBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface AnthropometricDataMapper extends ClinicalObservationMapper{

    @Named("fromAnthropometricDataDto")
    @Mapping(target = "bloodType", expression = "java(mapClinicalObs(anthropometricDataDto.getBloodType(), anthropometricDataDto.isDeleted()))")
    @Mapping(target = "height", expression = "java(mapClinicalObs(anthropometricDataDto.getHeight(), anthropometricDataDto.isDeleted()))")
    @Mapping(target = "weight", expression = "java(mapClinicalObs(anthropometricDataDto.getWeight(), anthropometricDataDto.isDeleted()))")
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
