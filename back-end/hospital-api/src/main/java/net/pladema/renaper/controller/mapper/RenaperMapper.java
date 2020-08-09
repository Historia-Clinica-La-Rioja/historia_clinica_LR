package net.pladema.renaper.controller.mapper;

import net.pladema.renaper.controller.dto.MedicalCoverageDto;
import net.pladema.renaper.controller.dto.PersonBasicDataResponseDto;
import net.pladema.renaper.services.domain.PersonDataResponse;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface RenaperMapper {

    @Mapping(target = "firstName", source = "nombres")
    @Mapping(target = "lastName", source = "apellido")
    @Mapping(target = "birthDate", source = "fechaNacimiento")
    public PersonBasicDataResponseDto fromPersonDataResponse(PersonDataResponse personDataResponse);

    @Named("fromPersonMedicalCoverageResponse")
    MedicalCoverageDto fromPersonMedicalCoverageResponse(PersonMedicalCoverageBo medicalCoverageData);

    @IterableMapping(qualifiedByName = "fromPersonMedicalCoverageResponse")
    List<MedicalCoverageDto> fromPersonMedicalCoverageResponseList(List<PersonMedicalCoverageBo> medicalCoverageDatas);
}
