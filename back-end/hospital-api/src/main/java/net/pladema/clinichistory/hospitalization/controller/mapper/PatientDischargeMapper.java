package net.pladema.clinichistory.hospitalization.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.dto.PatientDischargeDto;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface PatientDischargeMapper {


    @Named("toPatientDischargeDto")
    PatientDischargeDto toPatientDischargeDto(PatientDischargeBo patientDischarge);
    
    @Named("toPatientDischargeBo")
    PatientDischargeBo toPatientDischargeBo(PatientDischargeDto patientDischargeDto);

}