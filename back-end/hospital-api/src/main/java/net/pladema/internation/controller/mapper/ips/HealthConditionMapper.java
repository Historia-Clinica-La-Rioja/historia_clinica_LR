package net.pladema.internation.controller.mapper.ips;

import net.pladema.internation.controller.dto.ips.HealthConditionDto;
import net.pladema.internation.repository.ips.generalstate.HealthConditionVo;
import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryCondition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface HealthConditionMapper {

    @Named("toHealthConditionDto")
    HealthConditionDto toHealthConditionDto(HealthConditionBo healthConditionBo);

    @Named("toHealthConditionBo")
    HealthConditionBo toHealthConditionBo(HealthConditionVo healthConditionVo);

    @Named("toHealthHistoryCondition")
    @Mapping(target = "date", source = "startDate")
    HealthHistoryCondition toHealthHistoryCondition(HealthConditionVo healthConditionVo);
}
