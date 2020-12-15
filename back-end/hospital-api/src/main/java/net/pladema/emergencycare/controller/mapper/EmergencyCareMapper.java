package net.pladema.emergencycare.controller.mapper;

import net.pladema.emergencycare.controller.dto.EmergencyCareDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareListDto;
import net.pladema.emergencycare.controller.dto.administrative.ResponseEmergencyCareDto;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface EmergencyCareMapper {

    @Named("toEmergencyCareDto")
    EmergencyCareDto toEmergencyCareDto(EmergencyCareBo emergencyCareBo);

    @Named("toResponseEmergencyCareDto")
    ResponseEmergencyCareDto toResponseEmergencyCareDto(EmergencyCareBo emergencyCareBo);

    @Named("toEmergencyCareListDto")
    EmergencyCareListDto toEmergencyCareListDto(EmergencyCareBo emergencyCareBo);

    @Named("toListEmergencyCareListDto")
    @IterableMapping(qualifiedByName = "toEmergencyCareListDto")
    List<EmergencyCareListDto> toListEmergencyCareListDto(List<EmergencyCareBo> episodes);

    @Named("toEmergencyCareBo")
    EmergencyCareBo toEmergencyCareBo(EmergencyCareDto body);
}
