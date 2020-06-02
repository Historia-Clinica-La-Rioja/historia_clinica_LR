package net.pladema.internation.controller.internment.mapper;

import net.pladema.internation.controller.internment.dto.ResponsibleContactDto;
import net.pladema.internation.repository.internment.domain.ResponsibleContact;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface ResponsibleContactMapper {

    @Named("toResponsibleContact")
    ResponsibleContact toResponsibleContact (ResponsibleContactDto responsibleContactDto);

    @Named("toResponsibleContactDto")
    ResponsibleContactDto toResponsibleContactDto (ResponsibleContact responsibleContact);
}
