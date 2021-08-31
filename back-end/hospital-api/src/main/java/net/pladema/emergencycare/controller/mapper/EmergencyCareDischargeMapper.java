package net.pladema.emergencycare.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import net.pladema.emergencycare.controller.dto.AMedicalDischargeDto;
import net.pladema.emergencycare.controller.dto.VMedicalDischargeDto;
import net.pladema.emergencycare.service.domain.EpisodeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import net.pladema.emergencycare.controller.dto.AdministrativeDischargeDto;
import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class, DischargeTypeMasterDataMapper.class})
public interface EmergencyCareDischargeMapper {

    @Named("toMedicalDischargeBo")
    @Mapping(source = "medicalDischargeDto.problems", target = "problems")
    @Mapping(source = "patientInfo.id", target = "patientId")
    @Mapping(source = "patientInfo", target = "patientInfo")
    MedicalDischargeBo toMedicalDischargeBo(AMedicalDischargeDto medicalDischargeDto, Integer medicalDischargeBy, PatientInfoBo patientInfo, Integer sourceId);

    @Named("toAdministrativeDischargeBo")
    AdministrativeDischargeBo toAdministrativeDischargeBo(AdministrativeDischargeDto medicalDischargeDto, Integer episodeId, Integer userId);

    @Named("toMedicalDischargeDto")
    VMedicalDischargeDto toMedicalDischargeDto(EpisodeDischargeBo episodeDischargeBo);
}
