package net.pladema.clinichistory.generalstate.controller.mapper;

import net.pladema.clinichistory.generalstate.controller.dto.HCEPersonalHistoryDto;
import net.pladema.clinichistory.generalstate.service.domain.HCEPersonalHistoryBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface HCEGeneralStateMapper {

    @Named("toHCEPersonalHistoryDto")
    HCEPersonalHistoryDto toHCEPersonalHistoryDto(HCEPersonalHistoryBo source);

    @Named("toListHCEPersonalHistoryDto")
    @IterableMapping(qualifiedByName = "toHCEPersonalHistoryDto")
    List<HCEPersonalHistoryDto> toListHCEPersonalHistoryDto(List<HCEPersonalHistoryBo> sourceList);

}
