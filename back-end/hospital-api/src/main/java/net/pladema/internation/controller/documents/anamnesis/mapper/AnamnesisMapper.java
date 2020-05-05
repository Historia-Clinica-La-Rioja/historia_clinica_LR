package net.pladema.internation.controller.documents.anamnesis.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.documents.anamnesis.dto.AnamnesisDto;
import net.pladema.internation.controller.documents.anamnesis.dto.ResponseAnamnesisDto;
import net.pladema.internation.controller.ips.mapper.AnthropometricDataMapper;
import net.pladema.internation.controller.ips.mapper.VitalSignMapper;
import net.pladema.internation.service.documents.anamnesis.domain.Anamnesis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {VitalSignMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class})
public interface AnamnesisMapper {

    @Named("fromAnamnesisDto")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromAnthropometricDataDto")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromListAnthropometricDataDto")
    Anamnesis fromAnamnesisDto(AnamnesisDto anamnesisDto);

    @Named("fromAnamnesis")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataBo")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromVitalSignBo")
    ResponseAnamnesisDto fromAnamnesis(Anamnesis anamnesis);
}
