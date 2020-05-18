package net.pladema.internation.controller.ips.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.ips.dto.VitalSignDto;
import net.pladema.internation.controller.ips.dto.VitalSignsReportDto;
import net.pladema.internation.service.ips.domain.VitalSignBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface VitalSignMapper {

    @Named("fromVitalSignDto")

    VitalSignBo fromVitalSignDto(VitalSignDto vitalSignDto);

    @Named("fromVitalSignBo")
    VitalSignDto fromVitalSignBo(VitalSignBo vitalSignBo);

    @Named("toVitalSignsReportDto")
    VitalSignsReportDto toVitalSignsReportDto(VitalSignBo vitalSigns);


}
