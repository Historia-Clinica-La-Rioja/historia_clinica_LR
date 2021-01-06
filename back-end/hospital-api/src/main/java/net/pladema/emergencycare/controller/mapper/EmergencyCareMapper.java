package net.pladema.emergencycare.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.SnomedMapper;
import net.pladema.emergencycare.controller.dto.AdministrativeEmergencyCareDto;
import net.pladema.emergencycare.controller.dto.AdultGynecologicalEmergencyCareDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareListDto;
import net.pladema.emergencycare.controller.dto.PediatricEmergencyCareDto;
import net.pladema.emergencycare.controller.dto.administrative.ResponseEmergencyCareDto;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.triage.controller.mapper.TriageMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {TriageMapper.class, PoliceInterventionMapper.class, SnomedMapper.class})
public interface EmergencyCareMapper {

    @Named("toResponseEmergencyCareDto")
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
    EmergencyCareBo administrativeEmergencyCareDtoToEmergencyCareBo(AdministrativeEmergencyCareDto emergencyCareDto);

    @Named("adultGynecologicalEmergencyCareDtoToEmergencyCareBo")
    EmergencyCareBo adultGynecologicalEmergencyCareDtoToEmergencyCareBo(AdultGynecologicalEmergencyCareDto emergencyCareDto);

    @Named("pediatricEmergencyCareDtoToEmergencyCareBo")
    EmergencyCareBo pediatricEmergencyCareDtoToEmergencyCareBo(PediatricEmergencyCareDto emergencyCareDto);



}
