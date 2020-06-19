package net.pladema.clinichistory.hospitalization.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.dto.ResponsibleContactDto;
import net.pladema.clinichistory.hospitalization.service.domain.ResponsibleContactBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface ResponsibleContactMapper {

    @Named("toResponsibleContactBo")
    ResponsibleContactBo toResponsibleContactBo(ResponsibleContactDto responsibleContactDto);

    @Named("toResponsibleContactDto")
    ResponsibleContactDto toResponsibleContactDto(ResponsibleContactBo responsibleContact);

}
