package net.pladema.emergencycare.triage.controller.mapper;

import net.pladema.emergencycare.triage.controller.dto.TriageAdministrativeDto;
import net.pladema.emergencycare.triage.controller.dto.TriageAdultGynecologicalDto;
import net.pladema.emergencycare.triage.controller.dto.TriageCategoryDto;
import net.pladema.emergencycare.triage.controller.dto.TriageListDto;
import net.pladema.emergencycare.triage.controller.dto.TriagePediatricDto;
import net.pladema.emergencycare.triage.repository.entity.TriageCategory;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;
import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface TriageMapper {

    @Named("toTriageBo")
    TriageBo toTriageBo(TriageAdministrativeDto triageDto);

    @Named("toTriageBo")
    TriageBo toTriageBo(TriageAdultGynecologicalDto triageDto);

    @Named("toTriageBo")
    @Mapping(target = "bodyTemperatureId", source = "appearance.bodyTemperatureId")
    @Mapping(target = "cryingExcessive", source = "appearance.cryingExcessive")
    @Mapping(target = "muscleHypertoniaId", source = "appearance.muscleHypertonia")
    @Mapping(target = "respiratoryRetractionId", source = "breathing.respiratoryRetractionId")
    @Mapping(target = "stridor", source = "breathing.stridor")
    @Mapping(target = "perfusionId", source = "circulation.perfusionId")
    TriageBo toTriageBo(TriagePediatricDto triageDto);

    @Named("toTriageListDto")
    TriageListDto toTriageListDto(TriageBo triage);

    @Named("toListTriageListDto")
    @IterableMapping(qualifiedByName = "toTriageListDto")
    List<TriageListDto> toListTriageListDto(List<TriageBo> triages);

    @Named("toTriageCategoryDto")
    TriageCategoryDto toTriageCategoryDto(TriageCategory triageCategory);

    @Named("toListTriageCategoryDto")
    @IterableMapping(qualifiedByName = "toTriageCategoryDto")
    List<TriageCategoryDto> toListTriageCategoryDto(Collection<TriageCategory> data);
}
