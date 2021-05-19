package net.pladema.emergencycare.controller.mapper;

import net.pladema.emergencycare.controller.dto.PoliceInterventionDetailsDto;
import net.pladema.emergencycare.service.domain.PoliceInterventionDetailsBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface PoliceInterventionMapper {

    @Named("toPoliceInterventionDto")
	PoliceInterventionDetailsDto toPoliceInterventionDetailsDto(PoliceInterventionDetailsBo policeInterventionDetailsBo);

    @Named("toPoliceInterventionBo")
	PoliceInterventionDetailsBo toPoliceInterventionDetailsBo(PoliceInterventionDetailsDto policeInterventionDetailsDto);
}
