package net.pladema.internation.controller.mapper.ips;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.dto.ips.HealthConditionDto;
import net.pladema.internation.controller.dto.ips.HealthHistoryConditionDto;
import net.pladema.internation.repository.ips.generalstate.HealthConditionVo;
import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryConditionBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface HealthConditionMapper {

    @Named("toHealthConditionDto")
    HealthConditionDto toHealthConditionDto(HealthConditionBo healthConditionBo);

    @Named("toHealthHistoryConditionDto")
    HealthHistoryConditionDto toHealthHistoryConditionDto(HealthHistoryConditionBo healthConditionBo);

    @Named("toHealthConditionBo")
    HealthConditionBo toHealthConditionBo(HealthConditionVo healthConditionVo);

    @Named("toHealthHistoryCondition")
    @Mapping(target = "date", source = "startDate")
    HealthHistoryConditionBo toHealthHistoryCondition(HealthConditionVo healthConditionVo);
}
