package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SnomedMapper {

    @Mapping(target = "sctid", source = "id")
    SnomedBo fromSnomedDto(SnomedDto snomedDto);

    @Mapping(target = "id", source = "sctid")
    SnomedDto fromSnomedBo(SnomedBo snomedBo);

}
