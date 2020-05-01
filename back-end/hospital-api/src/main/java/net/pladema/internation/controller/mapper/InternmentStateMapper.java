package net.pladema.internation.controller.mapper;

import net.pladema.internation.controller.dto.core.InternmentGeneralStateDto;
import net.pladema.internation.controller.dto.ips.*;
import net.pladema.internation.controller.mapper.ips.AnthropometricDataMapper;
import net.pladema.internation.controller.mapper.ips.HealthConditionMapper;
import net.pladema.internation.controller.mapper.ips.MedicationMapper;
import net.pladema.internation.controller.mapper.ips.VitalSignMapper;
import net.pladema.internation.service.domain.InternmentGeneralState;
import net.pladema.internation.service.domain.ips.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {HealthConditionMapper.class, VitalSignMapper.class, AnthropometricDataMapper.class, MedicationMapper.class})
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

    @Named("toListMedicationDto")
    @IterableMapping(qualifiedByName = "toMedicationDto")
    List<MedicationDto> toListMedicationDto(List<MedicationBo> listMedicationBo);

    @Named("toInternmentGeneralStateDto")
    @Mapping(target = "diagnosis", source = "diagnosis", qualifiedByName = "toListHealthConditionDto")
    @Mapping(target = "personalHistories", source = "personalHistories", qualifiedByName = "toListHealthHistoryConditionDto")
    @Mapping(target = "familyHistories", source = "familyHistories", qualifiedByName = "toListHealthHistoryConditionDto")
    @Mapping(target = "vitalSigns", source = "vitalSigns", qualifiedByName = "toListVitalSignDto")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "toListMedicationDto")
    InternmentGeneralStateDto toInternmentGeneralStateDto(InternmentGeneralState interment);
}
