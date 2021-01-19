package net.pladema.emergencycare.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.SnomedMapper;
import net.pladema.emergencycare.controller.dto.ECAdministrativeDto;
import net.pladema.emergencycare.controller.dto.ECAdultGynecologicalDto;
import net.pladema.emergencycare.controller.dto.ECPediatricDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareListDto;
import net.pladema.emergencycare.controller.dto.ResponseEmergencyCareDto;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.triage.controller.mapper.TriageMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {TriageMapper.class, PoliceInterventionMapper.class, SnomedMapper.class, EEmergencyCareTypeMapper.class, EEmergencyCareEntranceMapper.class})
public interface EmergencyCareMapper {

    @Named("toResponseEmergencyCareDto")
    @Mapping(target = "entranceTypeId", source = "emergencyCareEntrance.id")
    @Mapping(target = "typeId", source = "emergencyCareType.id")
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "patient.patientMedicalCoverageId", source = "patientMedicalCoverageId")
    @Mapping(target = "policeIntervention", source = "policeIntervention", qualifiedByName = "toPoliceInterventionDto")
    ResponseEmergencyCareDto toResponseEmergencyCareDto(EmergencyCareBo emergencyCareBo);

    @Named("toEmergencyCareListDto")
    EmergencyCareListDto toEmergencyCareListDto(EmergencyCareBo emergencyCareBo);

    @Named("toListEmergencyCareListDto")
    @IterableMapping(qualifiedByName = "toEmergencyCareListDto")
    List<EmergencyCareListDto> toListEmergencyCareListDto(List<EmergencyCareBo> episodes);

    @Named("administrativeEmergencyCareDtoToEmergencyCareBo")
    @Mapping(target = "patientId", source = "administrative.patient.id")
    @Mapping(target = "patientMedicalCoverageId", source = "administrative.patient.patientMedicalCoverageId")
    @Mapping(target = "ambulanceCompanyId", source = "administrative.ambulanceCompanyId")
    @Mapping(target = "policeIntervention", source = "administrative.policeIntervention", qualifiedByName = "toPoliceInterventionBo")
    @Mapping(target = "triage", source = "triage", qualifiedByName = "toTriageBo")
    @Mapping(target = "emergencyCareType", source = "administrative.typeId", qualifiedByName = "fromEmergencyCareTypeId")
    @Mapping(target = "emergencyCareEntrance", source = "administrative.entranceTypeId", qualifiedByName = "fromEmergencyCareEntranceId")
    EmergencyCareBo administrativeEmergencyCareDtoToEmergencyCareBo(ECAdministrativeDto emergencyCareDto);

    @Named("adultGynecologicalEmergencyCareDtoToEmergencyCareBo")
    @Mapping(target = "patientId", source = "administrative.patient.id")
    @Mapping(target = "patientMedicalCoverageId", source = "administrative.patient.patientMedicalCoverageId")
    @Mapping(target = "policeIntervention", source = "administrative.policeIntervention", qualifiedByName = "toPoliceInterventionBo")
    @Mapping(target = "triage", source = "triage", qualifiedByName = "toTriageBo")
    @Mapping(target = "emergencyCareType", source = "administrative.typeId", qualifiedByName = "fromEmergencyCareTypeId")
    @Mapping(target = "emergencyCareEntrance", source = "administrative.entranceTypeId", qualifiedByName = "fromEmergencyCareEntranceId")
    EmergencyCareBo adultGynecologicalEmergencyCareDtoToEmergencyCareBo(ECAdultGynecologicalDto emergencyCareDto);

    @Named("pediatricEmergencyCareDtoToEmergencyCareBo")
    @Mapping(target = "patientId", source = "administrative.patient.id")
    @Mapping(target = "patientMedicalCoverageId", source = "administrative.patient.patientMedicalCoverageId")
    @Mapping(target = "policeIntervention", source = "administrative.policeIntervention", qualifiedByName = "toPoliceInterventionBo")
    @Mapping(target = "triage", source = "triage", qualifiedByName = "toTriageBo")
    @Mapping(target = "emergencyCareType", source = "administrative.typeId", qualifiedByName = "fromEmergencyCareTypeId")
    @Mapping(target = "emergencyCareEntrance", source = "administrative.entranceTypeId", qualifiedByName = "fromEmergencyCareEntranceId")
    EmergencyCareBo pediatricEmergencyCareDtoToEmergencyCareBo(ECPediatricDto emergencyCareDto);



}
