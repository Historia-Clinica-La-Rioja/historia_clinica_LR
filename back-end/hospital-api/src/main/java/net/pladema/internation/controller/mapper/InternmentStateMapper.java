package net.pladema.internation.controller.mapper;

import net.pladema.internation.controller.dto.core.InternmentGeneralStateDto;
import net.pladema.internation.controller.dto.ips.HealthConditionDto;
import net.pladema.internation.controller.dto.ips.HealthHistoryConditionDto;
import net.pladema.internation.controller.mapper.ips.HealthConditionMapper;
import net.pladema.internation.service.domain.InternmentGeneralState;
import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryCondition;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {HealthConditionMapper.class})
public interface InternmentStateMapper {

    @Named("toListHealthConditionDto")
    List<HealthConditionDto> toListHealthConditionDto(List<HealthConditionBo> listHealthConditionBo);

    @Named("toListHealthHistoryConditionDto")
    List<HealthHistoryConditionDto> toListHealthHistoryConditionDto(List<HealthHistoryCondition> listHealthHistoryCondition);

    @Named("toInternmentGeneralStateDto")
    InternmentGeneralStateDto toInternmentGeneralStateDto(InternmentGeneralState interment);
}
