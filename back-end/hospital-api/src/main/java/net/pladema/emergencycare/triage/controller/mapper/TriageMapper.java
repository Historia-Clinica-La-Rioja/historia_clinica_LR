package net.pladema.emergencycare.triage.controller.mapper;

import net.pladema.emergencycare.triage.controller.dto.TriageAdministrativeDto;
import net.pladema.emergencycare.triage.controller.dto.TriageAdultGynecologicalDto;
import net.pladema.emergencycare.triage.controller.dto.TriageDto;
import net.pladema.emergencycare.triage.controller.dto.TriageListDto;
import net.pladema.emergencycare.triage.controller.dto.TriagePediatricDto;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface TriageMapper {

    @Named("toTriageBo")
    TriageBo toTriageBo(TriageAdministrativeDto triageDto);

    @Named("toTriageBo")
    TriageBo toTriageBo(TriageAdultGynecologicalDto triageDto);

    @Named("toTriageBo")
    TriageBo toTriageBo(TriagePediatricDto triageDto);

    @Named("toTriageListDto")
    TriageListDto toTriageListDto(TriageBo triage);

    @Named("toListTriageListDto")
    @IterableMapping(qualifiedByName = "toTriageListDto")
    List<TriageListDto> toListTriageListDto(List<TriageBo> triages);
}
