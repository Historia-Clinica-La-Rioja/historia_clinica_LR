package net.pladema.internation.controller.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.dto.core.AnamnesisDto;
import net.pladema.internation.controller.dto.core.ResponseAnamnesisDto;
import net.pladema.internation.service.domain.Anamnesis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {VitalSignMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class})
public interface AnamnesisMapper {

    @Named("fromAnamnesisDto")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromListVitalSignDto")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromListAnthropometricDataDto")
    Anamnesis fromAnamnesisDto(AnamnesisDto anamnesisDto);

    @Named("fromAnamnesis")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromListAnthropometricDataBo")
    ResponseAnamnesisDto fromAnamnesis(Anamnesis anamnesis);
}
