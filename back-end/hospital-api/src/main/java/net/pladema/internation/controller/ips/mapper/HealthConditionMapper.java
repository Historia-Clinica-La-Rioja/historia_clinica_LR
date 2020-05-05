package net.pladema.internation.controller.ips.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.ips.dto.DiagnosisDto;
import net.pladema.internation.controller.ips.dto.HealthConditionDto;
import net.pladema.internation.controller.ips.dto.HealthHistoryConditionDto;
import net.pladema.internation.repository.ips.generalstate.HealthConditionVo;
import net.pladema.internation.service.ips.domain.DiagnosisBo;
import net.pladema.internation.service.ips.domain.HealthConditionBo;
import net.pladema.internation.service.ips.domain.HealthHistoryConditionBo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface HealthConditionMapper {

    @Named("toHealthConditionDto")
    HealthConditionDto toHealthConditionDto(HealthConditionBo healthConditionBo);

    @Named("toHealthHistoryConditionDto")
    HealthHistoryConditionDto toHealthHistoryConditionDto(HealthHistoryConditionBo healthConditionBo);

    @Named("toDiagnosisBo")
    DiagnosisBo toDiagnosisBo(HealthConditionVo healthConditionVo);

    @Named("toDiagnosisDto")
    DiagnosisDto toDiagnosisDto(DiagnosisBo diagnosisBo);

    @Named("toHealthHistoryCondition")
    @Mapping(target = "date", source = "startDate")
    HealthHistoryConditionBo toHealthHistoryConditionBo(HealthConditionVo healthConditionVo);
}
