package net.pladema.patient.controller.mapper;

import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface PatientMedicalCoverageMapper {

    @Named("toPatientMedicalCoverageDto")
    @Mapping(target = "medicalCoverage", expression = "java(patientMedicalCoverageBo.getMedicalCoverage().newInstance())")
    PatientMedicalCoverageDto toPatientMedicalCoverageDto(PatientMedicalCoverageBo patientMedicalCoverageBo);

    @Named("toListPatientMedicalCoverageDto")
    @IterableMapping(qualifiedByName = "toPatientMedicalCoverageDto")
    List<PatientMedicalCoverageDto> toListPatientMedicalCoverageDto(List<PatientMedicalCoverageBo> patientMedicalCoverageBo);

    @Named("toPatientMedicalCoverageBo")
    @Mapping(target = "medicalCoverage", expression = "java(patientMedicalCoverageDto.getMedicalCoverage().newInstance())")
    PatientMedicalCoverageBo toPatientMedicalCoverageBo(PatientMedicalCoverageDto patientMedicalCoverageDto);

    @Named("toListPatientMedicalCoverageBo")
    @IterableMapping(qualifiedByName = "toPatientMedicalCoverageBo")
    List<PatientMedicalCoverageBo> toListPatientMedicalCoverageBo(List<PatientMedicalCoverageDto> patientMedicalCoverageDtos);


}
