package net.pladema.emergencycare.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.emergencycare.triage.controller.dto.TriagePediatricDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface TriageRiskFactorMapper {

    @Named("fromTriagePediatricDto")
    @Mapping(target = "heartRate", source = "circulation.heartRate")
    @Mapping(target = "respiratoryRate", source = "breathing.respiratoryRate")
    @Mapping(target = "bloodOxygenSaturation", source = "breathing.bloodOxygenSaturation")
	NewRiskFactorsObservationDto fromTriagePediatricDto(TriagePediatricDto triagePediatricDto);

}