package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignBo;
import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignObservationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewVitalSignsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.VitalSignDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.VitalSignObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.VitalSignsReportDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(uses = {LocalDateMapper.class})
public interface VitalSignMapper {

    @Named("toVitalSignsObservationDto")
    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    NewVitalSignsObservationDto toVitalSignsObservationDto(VitalSignBo vitalSignBo);

    @Named("fromVitalSignsObservationDto")
    VitalSignBo fromVitalSignsObservationDto(NewVitalSignsObservationDto vitalSignsObservationDto);

    @Named("fromVitalSignDto")
    VitalSignBo fromVitalSignDto(VitalSignDto vitalSignDto);

    @Named("fromVitalSignBo")
    VitalSignDto fromVitalSignBo(VitalSignBo vitalSignBo);

    @Named("toVitalSignsReportDto")
    VitalSignsReportDto toVitalSignsReportDto(VitalSignBo vitalSigns);

    @Named("fromVitalSignObservationBo")
    @Mapping(target = "vitalSignObservation", source = "vitalSign")
    VitalSignObservationDto fromVitalSignObservationBo(VitalSignObservationBo vitalSignObservationBo);


}
