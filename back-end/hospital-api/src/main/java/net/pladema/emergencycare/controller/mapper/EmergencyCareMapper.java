package net.pladema.emergencycare.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import net.pladema.emergencycare.controller.dto.*;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.EmergencyCareEpisodeInProgressBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import net.pladema.emergencycare.triage.controller.mapper.TriageMapper;
import net.pladema.medicalconsultation.doctorsoffice.controller.mapper.DoctorsOfficeMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.masterdata.domain.EnumWriter;
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
    @Mapping(target = "policeInterventionDetails", source = "policeInterventionDetails", qualifiedByName = "toPoliceInterventionDto")
    @Mapping(target = "reasons", source = "reasons", qualifiedByName = "fromListReasonBo")
    @Mapping(target = "creationDate", source = "createdOn")
    @Mapping(target = "hasPoliceIntervention", source = "hasPoliceIntervention")
    ResponseEmergencyCareDto toResponseEmergencyCareDto(EmergencyCareBo emergencyCareBo);

    @AfterMapping
    default void masterDataResponseMapping(@MappingTarget ResponseEmergencyCareDto target, EmergencyCareBo emergencyCareBo) {
        target.setEmergencyCareState(EnumWriter.write(EEmergencyCareState.getById(emergencyCareBo.getEmergencyCareStateId())));
        target.setEmergencyCareType(EnumWriter.write(EEmergencyCareType.getById(emergencyCareBo.getEmergencyCareTypeId())));
        target.setEntranceType(EnumWriter.write(EEmergencyCareEntrance.getById(emergencyCareBo.getEmergencyCareEntranceId())));
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
        target.setType(EnumWriter.write(EEmergencyCareType.getById(emergencyCareBo.getEmergencyCareTypeId())));
        target.setState(EnumWriter.write(EEmergencyCareState.getById(emergencyCareBo.getEmergencyCareStateId())));
    }

    @Named("administrativeEmergencyCareDtoToEmergencyCareBo")
    @Mapping(target = "patient.id", source = "administrative.patient.id")
    @Mapping(target = "patient.patientMedicalCoverageId", source = "administrative.patient.patientMedicalCoverageId")
    @Mapping(target = "ambulanceCompanyId", source = "administrative.ambulanceCompanyId")
    @Mapping(target = "policeInterventionDetails", source = "administrative.policeInterventionDetails", qualifiedByName = "toPoliceInterventionBo")
    @Mapping(target = "doctorsOffice.id", source = "administrative.doctorsOfficeId")
    @Mapping(target = "triage", source = "triage", qualifiedByName = "toTriageBo")
    @Mapping(target = "emergencyCareTypeId", source = "administrative.emergencyCareTypeId")
    @Mapping(target = "emergencyCareEntranceId", source = "administrative.entranceTypeId")
    @Mapping(target = "hasPoliceIntervention", source = "administrative.hasPoliceIntervention")
    EmergencyCareBo administrativeEmergencyCareDtoToEmergencyCareBo(ECAdministrativeDto emergencyCareDto);

    @Named("administrativeUpdateEmergencyCareDtoToEmergencyCareBo")
    @Mapping(target = "patient.id", source = "patient.id")
    @Mapping(target = "patient.patientMedicalCoverageId", source = "patient.patientMedicalCoverageId")
    @Mapping(target = "ambulanceCompanyId", source = "ambulanceCompanyId")
    @Mapping(target = "policeInterventionDetails", source = "policeInterventionDetails", qualifiedByName = "toPoliceInterventionBo")
    @Mapping(target = "doctorsOffice.id", source = "doctorsOfficeId")
    @Mapping(target = "emergencyCareTypeId", source = "emergencyCareTypeId")
    @Mapping(target = "emergencyCareEntranceId", source = "entranceTypeId")
    @Mapping(target = "hasPoliceIntervention", source = "hasPoliceIntervention")
    EmergencyCareBo emergencyCareDtoToEmergencyCareBo(NewEmergencyCareDto updateEmergencyCareDto);

    @Named("adultGynecologicalEmergencyCareDtoToEmergencyCareBo")
    @Mapping(target = "patient.id", source = "administrative.patient.id")
    @Mapping(target = "patient.patientMedicalCoverageId", source = "administrative.patient.patientMedicalCoverageId")
    @Mapping(target = "ambulanceCompanyId", source = "administrative.ambulanceCompanyId")
    @Mapping(target = "policeInterventionDetails", source = "administrative.policeInterventionDetails", qualifiedByName = "toPoliceInterventionBo")
    @Mapping(target = "doctorsOffice.id", source = "administrative.doctorsOfficeId")
    @Mapping(target = "triage", source = "triage", qualifiedByName = "toTriageBo")
    @Mapping(target = "emergencyCareTypeId", source = "administrative.emergencyCareTypeId")
    @Mapping(target = "emergencyCareEntranceId", source = "administrative.entranceTypeId")
    @Mapping(target = "hasPoliceIntervention", source = "administrative.hasPoliceIntervention")
    EmergencyCareBo adultGynecologicalEmergencyCareDtoToEmergencyCareBo(ECAdultGynecologicalDto emergencyCareDto);

    @Named("pediatricEmergencyCareDtoToEmergencyCareBo")
    @Mapping(target = "patient.id", source = "administrative.patient.id")
    @Mapping(target = "patient.patientMedicalCoverageId", source = "administrative.patient.patientMedicalCoverageId")
    @Mapping(target = "ambulanceCompanyId", source = "administrative.ambulanceCompanyId")
    @Mapping(target = "policeInterventionDetails", source = "administrative.policeInterventionDetails", qualifiedByName = "toPoliceInterventionBo")
    @Mapping(target = "doctorsOffice.id", source = "administrative.doctorsOfficeId")
    @Mapping(target = "triage", source = "triage", qualifiedByName = "toTriageBo")
    @Mapping(target = "emergencyCareTypeId", source = "administrative.emergencyCareTypeId")
    @Mapping(target = "emergencyCareEntranceId", source = "administrative.entranceTypeId")
    @Mapping(target = "hasPoliceIntervention", source = "administrative.hasPoliceIntervention")
    EmergencyCareBo pediatricEmergencyCareDtoToEmergencyCareBo(ECPediatricDto emergencyCareDto);

    @Named("toEmergencyCareUserDto")
    @Mapping(target = "firstName", source = "personDto.firstName")
    @Mapping(target = "lastName", source = "personDto.lastName")
	@Mapping(target = "nameSelfDetermination", source = "personDto.nameSelfDetermination")
    EmergencyCareUserDto toEmergencyCareUserDto(UserDto userDto);

	@Named("toEmergencyCareEpisodeInProgressDto")
	EmergencyCareEpisodeInProgressDto toEmergencyCareEpisodeInProgressDto(EmergencyCareEpisodeInProgressBo emergencyCareEpisodeInProgressBo);


}
