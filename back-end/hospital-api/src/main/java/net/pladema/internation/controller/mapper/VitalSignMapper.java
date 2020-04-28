package net.pladema.internation.controller.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.dto.ips.VitalSignDto;
import net.pladema.internation.service.domain.ips.VitalSignBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface VitalSignMapper {

    @Named("fromVitalSignDto")
    @Mapping(target = "systolicBloodPressure.deleted", source = "deleted")
    @Mapping(target = "diastolicBloodPressure.deleted", source = "deleted")
    @Mapping(target = "temperature.deleted", source = "deleted")
    @Mapping(target = "heartRate.deleted", source = "deleted")
    @Mapping(target = "respiratoryRate.deleted", source = "deleted")
    @Mapping(target = "bloodOxygenSaturation.deleted", source = "deleted")
    VitalSignBo fromVitalSignDto(VitalSignDto vitalSignDto);

    @Named("fromListVitalSignDto")
    @IterableMapping(qualifiedByName = "fromVitalSignDto")
    List<VitalSignBo> fromListVitalSignDto(List<VitalSignDto> vitalSignDtos);

    @Named("fromVitalSignBo")
    VitalSignDto fromVitalSignBo(VitalSignBo vitalSignBo);

    @Named("fromListVitalSignBo")
    @IterableMapping(qualifiedByName = "fromVitalSignBo")
    List<VitalSignDto> fromListVitalSignBo(List<VitalSignBo> vitalSignBos);
}
