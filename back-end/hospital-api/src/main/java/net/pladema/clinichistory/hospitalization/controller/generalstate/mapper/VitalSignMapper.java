package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import net.pladema.clinichistory.documents.controller.dto.NewVitalSignsObservationDto;
import net.pladema.clinichistory.documents.controller.dto.VitalSignObservationDto;
import net.pladema.clinichistory.documents.service.ips.domain.VitalSignBo;
import net.pladema.clinichistory.documents.service.ips.domain.VitalSignObservationBo;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.VitalSignDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.VitalSignsReportDto;
import net.pladema.emergencycare.triage.controller.dto.TriagePediatricDto;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface VitalSignMapper {

    @Named("fromTriagePediatricDto")
    @Mapping(target = "heartRate", source = "circulation.heartRate")
    @Mapping(target = "respiratoryRate", source = "breathing.respiratoryRate")
    @Mapping(target = "bloodOxygenSaturation", source = "breathing.bloodOxygenSaturation")
    NewVitalSignsObservationDto fromTriagePediatricDto(TriagePediatricDto triagePediatricDto);

    @Named("toVitalSignsObservationDto")
    NewVitalSignsObservationDto toVitalSignsObservationDto(VitalSignBo vitalSignBo);

    @Named("fromVitalSignsObservationDto")
    VitalSignBo fromVitalSignsObservationDto(NewVitalSignsObservationDto vitalSignsObservationDto);

    @Named("fromVitalSignDto")
    VitalSignBo fromVitalSignDto(VitalSignDto vitalSignDto);

    @Named("fromVitalSignBo")
    VitalSignDto fromVitalSignBo(VitalSignBo vitalSignBo);

    @Named("toVitalSignsReportDto")
    VitalSignsReportDto toVitalSignsReportDto(VitalSignBo vitalSigns);

    @Named("fromVitalSignObservationBo")
    @Mapping(target = "vitalSignObservation", source = "vitalSign")
    VitalSignObservationDto fromVitalSignObservationBo(VitalSignObservationBo vitalSignObservationBo);


}
