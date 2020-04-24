package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.IdentificationTypeDto;
import net.pladema.person.repository.entity.IdentificationType;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface IdentificationTypeMapper {

    @Named("fromIdentificationType")
    IdentificationTypeDto fromIdentificationType(IdentificationType identificationType);

    List<IdentificationTypeDto> fromIdentificationTypeList(List<IdentificationType> identificationTypes);
}
