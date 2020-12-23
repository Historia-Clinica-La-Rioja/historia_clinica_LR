package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import org.mapstruct.Mapper;

@Mapper
public interface SnomedMapper {

    SnomedBo fromSnomedDto(SnomedDto snomedDto);

    SnomedDto fromSnomedBo(SnomedBo snomedBo);

}
