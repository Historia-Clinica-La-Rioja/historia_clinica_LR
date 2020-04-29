package net.pladema.internation.controller.mapper.ips;

import net.pladema.internation.controller.dto.ips.VitalSignDto;
import net.pladema.internation.service.domain.ips.VitalSignBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface VitalSignMapper extends ClinicalObservationMapper {

    @Named("fromVitalSignDto")
    @Mapping(target = "systolicBloodPressure", expression = "java(mapClinicalObs(vitalSignDto.getSystolicBloodPressure(), vitalSignDto.isDeleted()))")
    @Mapping(target = "diastolicBloodPressure", expression = "java(mapClinicalObs(vitalSignDto.getDiastolicBloodPressure(), vitalSignDto.isDeleted()))")
    @Mapping(target = "temperature", expression = "java(mapClinicalObs(vitalSignDto.getTemperature(), vitalSignDto.isDeleted()))")
    @Mapping(target = "heartRate", expression = "java(mapClinicalObs(vitalSignDto.getHeartRate(), vitalSignDto.isDeleted()))")
    @Mapping(target = "respiratoryRate", expression = "java(mapClinicalObs(vitalSignDto.getRespiratoryRate(), vitalSignDto.isDeleted()))")
    @Mapping(target = "bloodOxygenSaturation", expression = "java(mapClinicalObs(vitalSignDto.getBloodOxygenSaturation(), vitalSignDto.isDeleted()))")
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
