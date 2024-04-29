package net.pladema.emergencycare.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.emergencycare.controller.dto.AMedicalDischargeDto;
import net.pladema.emergencycare.controller.dto.AdministrativeDischargeDto;
import net.pladema.emergencycare.controller.dto.VMedicalDischargeDto;
import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;
import net.pladema.emergencycare.service.domain.EpisodeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class, DischargeTypeMasterDataMapper.class})
public interface EmergencyCareDischargeMapper {

    @Named("toMedicalDischargeBo")
    @Mapping(source = "medicalDischargeDto.problems", target = "problems")
    MedicalDischargeBo toMedicalDischargeBo(AMedicalDischargeDto medicalDischargeDto);

    @Named("toAdministrativeDischargeBo")
    AdministrativeDischargeBo toAdministrativeDischargeBo(AdministrativeDischargeDto medicalDischargeDto, Integer episodeId, Integer userId);

    @Named("toMedicalDischargeDto")
    VMedicalDischargeDto toMedicalDischargeDto(EpisodeDischargeBo episodeDischargeBo);
}
