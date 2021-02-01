package net.pladema.emergencycare.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.SnomedMapper;
import net.pladema.emergencycare.controller.dto.*;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import net.pladema.emergencycare.triage.controller.mapper.TriageMapper;
import net.pladema.medicalconsultation.doctorsoffice.controller.mapper.DoctorsOfficeMapper;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.sgx.masterdata.service.domain.EnumWriter;
import net.pladema.user.controller.dto.UserDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {TriageMapper.class, PoliceInterventionMapper.class, SnomedMapper.class, MasterDataMapper.class, LocalDateMapper.class, DoctorsOfficeMapper.class, SnomedMapper.class})
public interface EmergencyCareMapper {

    @Named("toResponseEmergencyCareDto")
    @Mapping(target = "entranceType", ignore = true)
    @Mapping(target = "emergencyCareType", ignore = true)
    @Mapping(target = "emergencyCareState", ignore = true)
    @Mapping(target = "policeIntervention", source = "policeIntervention", qualifiedByName = "toPoliceInterventionDto")
    @Mapping(target = "reasons", source = "reasons", qualifiedByName = "fromListReasonBo")
    @Mapping(target = "creationDate", source = "createdOn")
    ResponseEmergencyCareDto toResponseEmergencyCareDto(EmergencyCareBo emergencyCareBo);

    @AfterMapping
    default void masterDataResponseMapping(@MappingTarget ResponseEmergencyCareDto target, EmergencyCareBo emergencyCareBo) {
        target.setEmergencyCareState(EnumWriter.write(EEmergencyCareState.getById(emergencyCareBo.getEmergencyCareState())));
        target.setEmergencyCareType(EnumWriter.write(EEmergencyCareType.getById(emergencyCareBo.getEmergencyCareType())));
        target.setEntranceType(EnumWriter.write(EEmergencyCareEntrance.getById(emergencyCareBo.getEmergencyCareEntrance())));
    }

    @Named("toEmergencyCareListDto")
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "creationDate", source = "createdOn")
    @Mapping(target = "triage.id", source = "triageCategoryId")
    @Mapping(target = "triage.name", source = "triageName")
    @Mapping(target = "triage.color", source = "triageColorCode")
    EmergencyCareListDto toEmergencyCareListDto(EmergencyCareBo emergencyCareBo);

    @Named("toListEmergencyCareListDto")
    @IterableMapping(qualifiedByName = "toEmergencyCareListDto")
    List<EmergencyCareListDto> toListEmergencyCareListDto(List<EmergencyCareBo> episodes);

    @AfterMapping
    default void masterDataListMapping(@MappingTarget EmergencyCareListDto target, EmergencyCareBo emergencyCareBo) {
        target.setType(EnumWriter.write(EEmergencyCareType.getById(emergencyCareBo.getEmergencyCareType())));
        target.setState(EnumWriter.write(EEmergencyCareState.getById(emergencyCareBo.getEmergencyCareState())));
    }

    @Named("administrativeEmergencyCareDtoToEmergencyCareBo")
    @Mapping(target = "patient.id", source = "administrative.patient.id")
    @Mapping(target = "ambulanceCompanyId", source = "administrative.ambulanceCompanyId")
    @Mapping(target = "policeIntervention", source = "administrative.policeIntervention", qualifiedByName = "toPoliceInterventionBo")
    @Mapping(target = "triage", source = "triage", qualifiedByName = "toTriageBo")
    @Mapping(target = "emergencyCareType", source = "administrative.emergencyCareType", qualifiedByName = "fromMasterDataDto")
    @Mapping(target = "emergencyCareEntrance", source = "administrative.entranceType", qualifiedByName = "fromMasterDataDto")
    EmergencyCareBo administrativeEmergencyCareDtoToEmergencyCareBo(ECAdministrativeDto emergencyCareDto);

    @Named("adultGynecologicalEmergencyCareDtoToEmergencyCareBo")
    @Mapping(target = "patient.id", source = "administrative.patient.id")
    @Mapping(target = "policeIntervention", source = "administrative.policeIntervention", qualifiedByName = "toPoliceInterventionBo")
    @Mapping(target = "triage", source = "triage", qualifiedByName = "toTriageBo")
    @Mapping(target = "emergencyCareType", source = "administrative.emergencyCareType", qualifiedByName = "fromMasterDataDto")
    @Mapping(target = "emergencyCareEntrance", source = "administrative.entranceType", qualifiedByName = "fromMasterDataDto")
    EmergencyCareBo adultGynecologicalEmergencyCareDtoToEmergencyCareBo(ECAdultGynecologicalDto emergencyCareDto);

    @Named("pediatricEmergencyCareDtoToEmergencyCareBo")
    @Mapping(target = "patient.id", source = "administrative.patient.id")
    @Mapping(target = "policeIntervention", source = "administrative.policeIntervention", qualifiedByName = "toPoliceInterventionBo")
    @Mapping(target = "triage", source = "triage", qualifiedByName = "toTriageBo")
    @Mapping(target = "emergencyCareType", source = "administrative.emergencyCareType", qualifiedByName = "fromMasterDataDto")
    @Mapping(target = "emergencyCareEntrance", source = "administrative.entranceType", qualifiedByName = "fromMasterDataDto")
    EmergencyCareBo pediatricEmergencyCareDtoToEmergencyCareBo(ECPediatricDto emergencyCareDto);

    @Named("toEmergencyCareUserDto")
    @Mapping(target = "firstName", source = "personDto.firstName")
    @Mapping(target = "lastName", source = "personDto.lastName")
    EmergencyCareUserDto toEmergencyCareUserDto(UserDto userDto);


}
