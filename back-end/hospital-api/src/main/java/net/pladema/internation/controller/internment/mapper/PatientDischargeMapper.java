package net.pladema.internation.controller.internment.mapper;

import net.pladema.internation.controller.internment.dto.PatientDischargeDto;
import net.pladema.internation.service.internment.summary.domain.PatientDischargeBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface PatientDischargeMapper {


    @Named("toPatientDischargeDto")
    PatientDischargeDto toPatientDischargeDto(PatientDischargeBo patientDischarge);
    
    @Named("toPatientDischargeBo")
    PatientDischargeBo toPatientDischargeBo(PatientDischargeDto patientDischargeDto);

}