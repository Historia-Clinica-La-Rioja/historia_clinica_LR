package net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.AnamnesisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.ResponseAnamnesisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnthropometricDataMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.VitalSignMapper;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {VitalSignMapper.class, AnthropometricDataMapper.class, LocalDateMapper.class, SnomedMapper.class})
public interface AnamnesisMapper {

    @Named("fromAnamnesisDto")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromVitalSignDto")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataDto")
    AnamnesisBo fromAnamnesisDto(AnamnesisDto anamnesisDto);

    @Named("fromAnamnesis")
    @Mapping(target = "anthropometricData", source = "anthropometricData", qualifiedByName = "fromAnthropometricDataBo")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "fromVitalSignBo")
    ResponseAnamnesisDto fromAnamnesis(AnamnesisBo anamnesisBo);
}
