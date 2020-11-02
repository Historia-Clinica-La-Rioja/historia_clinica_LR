package net.pladema.renaper.controller.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import net.pladema.renaper.controller.dto.MedicalCoverageDto;
import net.pladema.renaper.controller.dto.PersonBasicDataResponseDto;
import net.pladema.renaper.services.domain.PersonDataResponse;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import net.pladema.sgx.healthinsurance.service.impl.HealthInsuranceRnosGenerator;

@Mapper(imports = {HealthInsuranceRnosGenerator.class})
public interface RenaperMapper {

    @Mapping(target = "firstName", source = "nombres")
    @Mapping(target = "lastName", source = "apellido")
    @Mapping(target = "birthDate", source = "fechaNacimiento")
    @Mapping(target = "photo", source = "foto")
    public PersonBasicDataResponseDto fromPersonDataResponse(PersonDataResponse personDataResponse);

    @Named("fromPersonMedicalCoverageResponse")
    @Mapping(target="rnos", expression = "java(Integer.toString(HealthInsuranceRnosGenerator.calculateRnos(medicalCoverageData)))")
    MedicalCoverageDto fromPersonMedicalCoverageResponse(PersonMedicalCoverageBo medicalCoverageData);

    @IterableMapping(qualifiedByName = "fromPersonMedicalCoverageResponse")
    List<MedicalCoverageDto> fromPersonMedicalCoverageResponseList(List<PersonMedicalCoverageBo> medicalCoverageDatas);
}
