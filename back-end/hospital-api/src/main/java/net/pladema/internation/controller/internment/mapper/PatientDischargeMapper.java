package net.pladema.internation.controller.internment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import net.pladema.internation.controller.internment.dto.PatientDischargeDto;
import net.pladema.internation.repository.core.entity.PatientDischarge;

@Mapper
public interface PatientDischargeMapper {


    @Named("toPatientDischargeDto")
    PatientDischargeDto toPatientDischargeDto(PatientDischarge patientDischarge);
    
    @Named("toPatientDischarge")
    PatientDischarge toPatientDischarge(PatientDischargeDto patientDischargeDto);

}