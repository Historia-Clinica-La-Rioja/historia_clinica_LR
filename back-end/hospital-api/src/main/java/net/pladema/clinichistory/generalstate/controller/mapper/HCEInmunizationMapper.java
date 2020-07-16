package net.pladema.clinichistory.generalstate.controller.mapper;

import net.pladema.clinichistory.generalstate.controller.dto.HCEInmunizationDto;
import net.pladema.clinichistory.generalstate.service.domain.HCEInmunizationBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface HCEInmunizationMapper {

    @Named("toHCEInmunizationDto")
    HCEInmunizationDto toHCEInmunizationDto(HCEInmunizationBo source);

    @Named("toListHCEInmunizationDto")
    @IterableMapping(qualifiedByName = "toHCEInmunizationDto")
    List<HCEInmunizationDto> toListHCEInmunizationDto(List<HCEInmunizationBo> sourceList);
}
