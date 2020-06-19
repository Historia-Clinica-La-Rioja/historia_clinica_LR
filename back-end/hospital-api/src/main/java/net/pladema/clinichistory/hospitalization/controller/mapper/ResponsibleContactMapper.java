package net.pladema.clinichistory.hospitalization.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.dto.ResponsibleContactDto;
import net.pladema.clinichistory.hospitalization.repository.domain.ResponsibleContact;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface ResponsibleContactMapper {

    @Named("toResponsibleContact")
    ResponsibleContact toResponsibleContact (ResponsibleContactDto responsibleContactDto);

    @Named("toResponsibleContactDto")
    ResponsibleContactDto toResponsibleContactDto (ResponsibleContact responsibleContact);
}
