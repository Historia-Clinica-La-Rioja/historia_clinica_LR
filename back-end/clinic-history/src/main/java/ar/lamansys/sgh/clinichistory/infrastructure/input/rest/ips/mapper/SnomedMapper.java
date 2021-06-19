package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Named("fromReasonBo")
    @Mapping(target = "sctid", source = "snomed.sctid")
    @Mapping(target = "pt", source = "snomed.pt")
    @Mapping(target = "id", ignore = true)
    SnomedDto fromReasonBo(ReasonBo reasonBo);

    @Named("fromListReasonBo")
    @IterableMapping(qualifiedByName = "fromReasonBo")
    List<SnomedDto> fromListReasonBo(List<ReasonBo> reasonBos);

    @Named("toReasonBo")
    @Mapping(target = "snomed.sctid", source = "sctid")
    @Mapping(target = "snomed.pt", source = "pt")
    ReasonBo toReasonBo(SnomedDto snomedDto);

    @Named("toListReasonBo")
    @IterableMapping(qualifiedByName = "toReasonBo")
    List<ReasonBo> toListReasonBo(List<SnomedDto> snomedDto);
}
