package net.pladema.internation.controller.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.dto.core.AnamnesisDto;
import net.pladema.internation.controller.dto.core.ResponseAnamnesisDto;
import net.pladema.internation.service.domain.Anamnesis;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface AnamnesisMapper {

    @Named("fromAnamnesisDto")
    Anamnesis fromAnamnesisDto(AnamnesisDto anamnesisDto);

    @Named("fromAnamnesis")
    ResponseAnamnesisDto fromAnamnesis(Anamnesis anamnesis);
}
