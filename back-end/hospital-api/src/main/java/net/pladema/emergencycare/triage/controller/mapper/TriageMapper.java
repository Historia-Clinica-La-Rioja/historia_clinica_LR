package net.pladema.emergencycare.triage.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import net.pladema.emergencycare.triage.controller.dto.TriageAdministrativeDto;
import net.pladema.emergencycare.triage.controller.dto.TriageAdultGynecologicalDto;
import net.pladema.emergencycare.triage.controller.dto.TriageAppearanceDto;
import net.pladema.emergencycare.triage.controller.dto.TriageBreathingDto;
import net.pladema.emergencycare.triage.controller.dto.TriageCategoryDto;
import net.pladema.emergencycare.triage.controller.dto.TriageCirculationDto;
import net.pladema.emergencycare.triage.controller.dto.TriageListDto;
import net.pladema.emergencycare.triage.controller.dto.TriagePediatricDto;
import net.pladema.emergencycare.triage.repository.entity.TriageCategory;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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
    @Mapping(target = "muscleHypertoniaId", source = "appearance.muscleHypertoniaId")
    @Mapping(target = "respiratoryRetractionId", source = "breathing.respiratoryRetractionId")
    @Mapping(target = "stridor", source = "breathing.stridor")
    @Mapping(target = "perfusionId", source = "circulation.perfusionId")
    TriageBo toTriageBo(TriagePediatricDto triageDto);

    @Named("toTriageListDto")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", source = "createdOn")
    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "appearance.bodyTemperature.id", source = "bodyTemperatureId")
    @Mapping(target = "appearance.bodyTemperature.description", source = "bodyTemperatureDescription")
    @Mapping(target = "appearance.cryingExcessive", source = "cryingExcessive")
    @Mapping(target = "appearance.muscleHypertonia.id", source = "muscleHypertoniaId")
    @Mapping(target = "appearance.muscleHypertonia.description", source = "muscleHypertoniaDescription")
    @Mapping(target = "breathing.respiratoryRetraction.id", source = "respiratoryRetractionId")
    @Mapping(target = "breathing.respiratoryRetraction.description", source = "respiratoryRetractionDescription")
    @Mapping(target = "breathing.stridor", source = "stridor")
    @Mapping(target = "circulation.perfusion.id", source = "perfusionId")
    @Mapping(target = "circulation.perfusion.description", source = "perfusionDescription")
    TriageListDto toTriageListDto(TriageBo triage);

    @AfterMapping
    default TriageListDto doAfterMapping(@MappingTarget TriageListDto triageListDto) {
        if (triageListDto.getBreathing().hasNoValues())
            triageListDto.setBreathing(null);
        if (triageListDto.getAppearance().hasNoValues())
            triageListDto.setAppearance(null);
        if (triageListDto.getCirculation().hasNoValues())
            triageListDto.setCirculation(null);
        return triageListDto;
    }

    @AfterMapping
    default TriageBreathingDto doAfterMapping(@MappingTarget TriageBreathingDto triageBreathingDto) {
        if (triageBreathingDto.getRespiratoryRetraction().hasNoValues())
            triageBreathingDto.setRespiratoryRetraction(null);
        return triageBreathingDto;
    }

    @AfterMapping
    default TriageCirculationDto doAfterMapping(@MappingTarget TriageCirculationDto triageCirculationDto) {
        if (triageCirculationDto.getPerfusion().hasNoValues())
            triageCirculationDto.setPerfusion(null);
        return triageCirculationDto;
    }

    @AfterMapping
    default TriageAppearanceDto doAfterMapping(@MappingTarget TriageAppearanceDto triageAppearanceDto) {
        if (triageAppearanceDto.getBodyTemperature().hasNoValues())
            triageAppearanceDto.setBodyTemperature(null);
        if (triageAppearanceDto.getMuscleHypertonia().hasNoValues())
            triageAppearanceDto.setMuscleHypertonia(null);
        return triageAppearanceDto;
    }

    @Named("toListTriageListDto")
    @IterableMapping(qualifiedByName = "toTriageListDto")
    List<TriageListDto> toListTriageListDto(List<TriageBo> triages);

    @Named("toTriageCategoryDto")
    TriageCategoryDto toTriageCategoryDto(TriageCategory triageCategory);

    @Named("toListTriageCategoryDto")
    @IterableMapping(qualifiedByName = "toTriageCategoryDto")
    List<TriageCategoryDto> toListTriageCategoryDto(Collection<TriageCategory> data);

    @Named("fromTriagePediatricDto")
    @Mapping(target = "heartRate", source = "circulation.heartRate")
    @Mapping(target = "respiratoryRate", source = "breathing.respiratoryRate")
    @Mapping(target = "bloodOxygenSaturation", source = "breathing.bloodOxygenSaturation")
	NewRiskFactorsObservationDto fromTriagePediatricDto(TriagePediatricDto triagePediatricDto);
}
