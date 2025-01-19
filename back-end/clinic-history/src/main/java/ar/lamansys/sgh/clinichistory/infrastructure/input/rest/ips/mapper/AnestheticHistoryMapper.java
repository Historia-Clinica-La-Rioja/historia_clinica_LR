package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnesthesiaZone;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EPreviousAnesthesiaState;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnestheticHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(imports = {EPreviousAnesthesiaState.class, EAnesthesiaZone.class})
public interface AnestheticHistoryMapper {

    @Named("toAnestheticHistoryDto")
    AnestheticHistoryDto toAnestheticHistoryDto(AnestheticHistoryBo anestheticHistoryBo);

    @Named("toAnestheticHistoryBo")
    @Mapping(target = "state", expression = "java(anestheticHistoryDto.getStateId() != null ? EPreviousAnesthesiaState.map(anestheticHistoryDto.getStateId()) : null)")
    @Mapping(target = "zone", expression = "java(anestheticHistoryDto.getZoneId() != null ? EAnesthesiaZone.map(anestheticHistoryDto.getZoneId()) : null)")
    AnestheticHistoryBo toAnestheticHistoryBo(AnestheticHistoryDto anestheticHistoryDto);

}
