package net.pladema.renaper.controller.mapper;

import net.pladema.renaper.controller.dto.PersonBasicDataResponseDto;
import net.pladema.renaper.services.domain.PersonDataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RenaperMapper {

    @Mapping(target = "firstName", source = "nombres")
    @Mapping(target = "lastName", source = "apellido")
    @Mapping(target = "birthDate", source = "fechaNacimiento")
    public PersonBasicDataResponseDto fromPersonDataResponse(PersonDataResponse personDataResponse);
}
