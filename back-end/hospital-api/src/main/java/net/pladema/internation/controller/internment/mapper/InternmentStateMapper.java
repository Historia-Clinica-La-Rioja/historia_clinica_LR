package net.pladema.internation.controller.internment.mapper;

import net.pladema.internation.controller.internment.dto.InternmentGeneralStateDto;
import net.pladema.internation.controller.internment.dto.Last2VitalSignsDto;
import net.pladema.internation.controller.ips.dto.*;
import net.pladema.internation.controller.ips.mapper.AllergyConditionMapper;
import net.pladema.internation.controller.ips.mapper.AnthropometricDataMapper;
import net.pladema.internation.controller.ips.mapper.HealthConditionMapper;
import net.pladema.internation.controller.ips.mapper.InmunizationMapper;
import net.pladema.internation.controller.ips.mapper.MedicationMapper;
import net.pladema.internation.controller.ips.mapper.VitalSignMapper;
import net.pladema.internation.service.internment.domain.InternmentGeneralState;
import net.pladema.internation.service.internment.domain.Last2VitalSignsBo;
import net.pladema.internation.service.ips.domain.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {HealthConditionMapper.class, VitalSignMapper.class, AnthropometricDataMapper.class,
        MedicationMapper.class, InmunizationMapper.class, AllergyConditionMapper.class})
public interface InternmentStateMapper {

    @Named("toDiagnosisDto")
    @IterableMapping(qualifiedByName = "toDiagnosisDto")
    List<DiagnosisDto> toListDiagnosisDto(List<DiagnosisBo> listDiagnosisBo);

    @Named("toListHealthHistoryConditionDto")
    @IterableMapping(qualifiedByName = "toHealthHistoryConditionDto")
    List<HealthHistoryConditionDto> toListHealthHistoryConditionDto(List<HealthHistoryConditionBo> listHealthHistoryCondition);

    @Named("toListVitalSignDto")
    @IterableMapping(qualifiedByName = "fromVitalSignBo")
    List<VitalSignDto> toListVitalSignDto(List<VitalSignBo> vitalSignBos);

    @Named("toListAnthropometricDataDto")
    @IterableMapping(qualifiedByName = "fromAnthropometricDataBo")
    List<AnthropometricDataDto> toListAnthropometricDataDto(List<AnthropometricDataBo> anthropometricDatas);

    @Named("toListMedicationDto")
    @IterableMapping(qualifiedByName = "toMedicationDto")
    List<MedicationDto> toListMedicationDto(List<MedicationBo> listMedicationBo);

    @Named("toListInmunizationDto")
    @IterableMapping(qualifiedByName = "toInmunizationDto")
    List<InmunizationDto> toListInmunizationDto(List<InmunizationBo> inmunizationBos);
    
    @Named("toListAllergyConditionDto")
    @IterableMapping(qualifiedByName = "toAllergyConditionDto")
    List<AllergyConditionDto> toListAllergyConditionDto(List<AllergyConditionBo> allergyConditionBos);

    @Named("toInternmentGeneralStateDto")
    @Mapping(target = "diagnosis", source = "diagnosis", qualifiedByName = "toListHealthConditionDto")
    @Mapping(target = "personalHistories", source = "personalHistories", qualifiedByName = "toListHealthHistoryConditionDto")
    @Mapping(target = "familyHistories", source = "familyHistories", qualifiedByName = "toListHealthHistoryConditionDto")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "toListVitalSignDto")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "toListMedicationDto")
    InternmentGeneralStateDto toInternmentGeneralStateDto(InternmentGeneralState interment);

    @Named("toAnthropometricDataDto")
    AnthropometricDataDto toAnthropometricDataDto(AnthropometricDataBo anthropometricData);

    @Named("toHealthConditionDto")
    HealthConditionDto toHealthConditionDto(HealthConditionBo mainDiagnosis);

    @Named("toLast2VitalSignDto")
    Last2VitalSignsDto toLast2VitalSignDto(Last2VitalSignsBo vitalSignBos);
}
