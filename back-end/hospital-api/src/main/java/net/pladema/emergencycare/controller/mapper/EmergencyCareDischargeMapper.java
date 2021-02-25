package net.pladema.emergencycare.controller.mapper;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.SnomedMapper;
import net.pladema.emergencycare.controller.dto.MedicalDischargeDto;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import net.pladema.emergencycare.controller.dto.AdministrativeDischargeDto;
import net.pladema.emergencycare.controller.dto.MedicalDischargeDto;
import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface EmergencyCareDischargeMapper {

    @Named("toMedicalDischargeBo")
    @Mapping(source = "medicalDischargeDto.problems", target = "problems")
    @Mapping(source = "patientInfoBo.id", target = "patientId")
    @Mapping(source = "patientInfoBo", target = "patientInfoBo")
    MedicalDischargeBo toMedicalDischargeBo(MedicalDischargeDto medicalDischargeDto, Integer medicalDischargeBy, PatientInfoBo patientInfoBo, Integer sourceId);

    @Named("toAdministrativeDischargeBo")
    AdministrativeDischargeBo toAdministrativeDischargeBo(AdministrativeDischargeDto medicalDischargeDto, Integer episodeId, Integer userId);

}
