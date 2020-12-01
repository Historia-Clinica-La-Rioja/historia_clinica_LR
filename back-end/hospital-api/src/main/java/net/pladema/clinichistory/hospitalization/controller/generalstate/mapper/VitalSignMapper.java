package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.VitalSignDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.VitalSignsReportDto;
import net.pladema.clinichistory.documents.service.ips.domain.VitalSignBo;
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
