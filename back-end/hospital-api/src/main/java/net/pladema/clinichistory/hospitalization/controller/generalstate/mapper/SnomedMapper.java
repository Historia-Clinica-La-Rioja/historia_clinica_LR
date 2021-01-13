package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface SnomedMapper {

    @Named("fromSnomedDtoList")
    default List<SnomedBo> fromSnomedDtoList(List<SnomedDto> snomedDtoList){
        if (snomedDtoList == null)
            return new ArrayList<>();
        return snomedDtoList.stream()
                .map(this::fromSnomedDto)
                .collect(Collectors.toList());
    }

    @Named("fromSnomedDto")
    SnomedBo fromSnomedDto(SnomedDto snomedDto);

    @Named("fromSnomedBo")
    SnomedDto fromSnomedBo(SnomedBo snomedBo);
}
