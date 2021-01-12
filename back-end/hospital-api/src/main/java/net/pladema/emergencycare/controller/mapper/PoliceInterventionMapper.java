package net.pladema.emergencycare.controller.mapper;

import net.pladema.emergencycare.controller.dto.PoliceInterventionDto;
import net.pladema.emergencycare.service.domain.PoliceInterventionBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface PoliceInterventionMapper {

    @Named("toPoliceInterventionDto")
    PoliceInterventionDto toPoliceInterventionDto(PoliceInterventionBo policeInterventionBo);

    @Named("toPoliceInterventionBo")
    PoliceInterventionBo toPoliceInterventionBo(PoliceInterventionDto policeInterventionDto);
}
