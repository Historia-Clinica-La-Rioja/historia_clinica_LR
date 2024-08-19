package net.pladema.emergencycare.triage.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper.OutpatientConsultationMapper;
import net.pladema.emergencycare.triage.controller.dto.TriageAdministrativeDto;
import net.pladema.emergencycare.triage.infrastructure.input.rest.dto.TriageAdultGynecologicalDto;
import net.pladema.emergencycare.triage.controller.dto.TriageAppearanceDto;
import net.pladema.emergencycare.triage.controller.dto.TriageBreathingDto;
import net.pladema.emergencycare.triage.controller.dto.TriageCategoryDto;
import net.pladema.emergencycare.triage.controller.dto.TriageCirculationDto;
import net.pladema.emergencycare.triage.controller.dto.TriageListDto;
import net.pladema.emergencycare.triage.controller.dto.TriagePediatricDto;
import net.pladema.emergencycare.triage.repository.entity.TriageCategory;
import net.pladema.emergencycare.triage.domain.TriageBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Collection;
import java.util.List;

@Mapper(uses = {LocalDateMapper.class, OutpatientConsultationMapper.class})
public interface TriageMapper {

    @Named("toTriageBo")
	@Mapping(target = "clinicalSpecialtySectorBo.id", source = "clinicalSpecialtySectorId")
    TriageBo toTriageBo(TriageAdministrativeDto triageDto);

    @Named("toTriageBo")
    @Mapping(target = "notes", expression = "java(triageDto.getNotes())")
	@Mapping(target = "clinicalSpecialtySectorBo.id", source = "clinicalSpecialtySectorId")
    TriageBo toTriageBo(TriageAdultGynecologicalDto triageDto);

    @Named("toTriageBo")
    @Mapping(target = "otherRiskFactors.bodyTemperatureId", source = "appearance.bodyTemperatureId")
    @Mapping(target = "otherRiskFactors.cryingExcessive", source = "appearance.cryingExcessive")
    @Mapping(target = "otherRiskFactors.muscleHypertoniaId", source = "appearance.muscleHypertoniaId")
    @Mapping(target = "otherRiskFactors.respiratoryRetractionId", source = "breathing.respiratoryRetractionId")
    @Mapping(target = "otherRiskFactors.stridor", source = "breathing.stridor")
    @Mapping(target = "otherRiskFactors.perfusionId", source = "circulation.perfusionId")
    @Mapping(target = "notes", expression = "java(triageDto.getNotes())")
	@Mapping(target = "clinicalSpecialtySectorBo.id", source = "clinicalSpecialtySectorId")
    TriageBo toTriageBo(TriagePediatricDto triageDto);

    @Named("toTriageListDto")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", source = "createdOn")
    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "appearance.bodyTemperature.id", source = "otherRiskFactors.bodyTemperatureId")
    @Mapping(target = "appearance.bodyTemperature.description", source = "otherRiskFactors.bodyTemperatureDescription")
    @Mapping(target = "appearance.cryingExcessive", source = "otherRiskFactors.cryingExcessive")
    @Mapping(target = "appearance.muscleHypertonia.id", source = "otherRiskFactors.muscleHypertoniaId")
    @Mapping(target = "appearance.muscleHypertonia.description", source = "otherRiskFactors.muscleHypertoniaDescription")
    @Mapping(target = "breathing.respiratoryRetraction.id", source = "otherRiskFactors.respiratoryRetractionId")
    @Mapping(target = "breathing.respiratoryRetraction.description", source = "otherRiskFactors.respiratoryRetractionDescription")
    @Mapping(target = "breathing.stridor", source = "otherRiskFactors.stridor")
    @Mapping(target = "circulation.perfusion.id", source = "otherRiskFactors.perfusionId")
    @Mapping(target = "circulation.perfusion.description", source = "otherRiskFactors.perfusionDescription")
	@Mapping(target = "notes", source = "notes.otherNote")
	@Mapping(target = "id", source = "triageId")
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
