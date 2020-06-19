package net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.mapper;

import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.AnamnesisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.ResponseAnamnesisDto;
import net.pladema.clinichistory.ips.controller.mapper.AnthropometricDataMapper;
import net.pladema.clinichistory.ips.controller.mapper.VitalSignMapper;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {VitalSignMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class})
public interface AnamnesisMapper {

    @Named("fromAnamnesisDto")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromAnthropometricDataDto")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromListAnthropometricDataDto")
    AnamnesisBo fromAnamnesisDto(AnamnesisDto anamnesisDto);

    @Named("fromAnamnesis")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataBo")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromVitalSignBo")
    ResponseAnamnesisDto fromAnamnesis(AnamnesisBo anamnesisBo);
}
