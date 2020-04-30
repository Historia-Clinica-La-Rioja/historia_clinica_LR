package net.pladema.internation.controller.mapper;

import net.pladema.internation.controller.dto.core.InternmentGeneralStateDto;
import net.pladema.internation.controller.dto.ips.HealthConditionDto;
import net.pladema.internation.controller.dto.ips.HealthHistoryConditionDto;
import net.pladema.internation.controller.dto.ips.MedicationDto;
import net.pladema.internation.controller.dto.ips.VitalSignDto;
import net.pladema.internation.controller.mapper.ips.AnthropometricDataMapper;
import net.pladema.internation.controller.mapper.ips.HealthConditionMapper;
import net.pladema.internation.controller.mapper.ips.MedicationMapper;
import net.pladema.internation.controller.mapper.ips.VitalSignMapper;
import net.pladema.internation.service.domain.InternmentGeneralState;
import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryConditionBo;
import net.pladema.internation.service.domain.ips.MedicationBo;
import net.pladema.internation.service.domain.ips.VitalSignBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {HealthConditionMapper.class, VitalSignMapper.class, AnthropometricDataMapper.class, MedicationMapper.class})
public interface InternmentStateMapper {

    @Named("toListHealthConditionDto")
    @IterableMapping(qualifiedByName = "toHealthConditionDto")
    List<HealthConditionDto> toListHealthConditionDto(List<HealthConditionBo> listHealthConditionBo);

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
