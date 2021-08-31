package net.pladema.clinichistory.hospitalization.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.*;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.*;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.*;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentGeneralStateDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosesGeneralStateDto;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.HospitalizationGeneralState;
import ar.lamansys.sgh.clinichistory.domain.ips.Last2VitalSignsBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {HealthConditionMapper.class, VitalSignMapper.class, AnthropometricDataMapper.class,
        MedicationMapper.class, ImmunizationMapper.class, AllergyConditionMapper.class})
public interface InternmentStateMapper {

    @Named("toListDiagnosisDto")
    @IterableMapping(qualifiedByName = "toDiagnosisDto")
    List<DiagnosisDto> toListDiagnosisDto(List<DiagnosisBo> listDiagnosisBo);

    @Named("toListHealthHistoryConditionDto")
    @IterableMapping(qualifiedByName = "toHealthHistoryConditionDto")
    List<HealthHistoryConditionDto> toListHealthHistoryConditionDto(List<HealthHistoryConditionBo> listHealthHistoryCondition);

    @Named("toListDiagnosesGeneralStateDto")
    @IterableMapping(qualifiedByName = "toDiagnosesGeneralStateDto")
    List<DiagnosesGeneralStateDto> toListDiagnosesGeneralStateDto(List<HealthConditionBo> diagnoses);

    @Named("toListMedicationDto")
    @IterableMapping(qualifiedByName = "toMedicationDto")
    List<MedicationDto> toListMedicationDto(List<MedicationBo> listMedicationBo);

    @Named("toListImmunizationDto")
    @IterableMapping(qualifiedByName = "toImmunizationDto")
    List<ImmunizationDto> toListImmunizationDto(List<ImmunizationBo> immunizationBos);
    
    @Named("toListAllergyConditionDto")
    @IterableMapping(qualifiedByName = "toAllergyConditionDto")
    List<AllergyConditionDto> toListAllergyConditionDto(List<AllergyConditionBo> allergyConditionBos);

    @Named("toInternmentGeneralStateDto")
    @Mapping(target = "diagnosis", source = "diagnosis", qualifiedByName = "toListDiagnosisDto")
    @Mapping(target = "personalHistories", source = "personalHistories", qualifiedByName = "toListHealthHistoryConditionDto")
    @Mapping(target = "familyHistories", source = "familyHistories", qualifiedByName = "toListHealthHistoryConditionDto")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "toListVitalSignDto")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "toListMedicationDto")
    InternmentGeneralStateDto toInternmentGeneralStateDto(HospitalizationGeneralState interment);

    @Named("toAnthropometricDataDto")
    AnthropometricDataDto toAnthropometricDataDto(AnthropometricDataBo anthropometricData);

    @Named("toHealthConditionDto")
    HealthConditionDto toHealthConditionDto(HealthConditionBo mainDiagnosis);

    @Named("toLast2VitalSignDto")
    Last2VitalSignsDto toLast2VitalSignDto(Last2VitalSignsBo vitalSignBos);
}
