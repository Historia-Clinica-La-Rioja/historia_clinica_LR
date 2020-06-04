package net.pladema.internation.controller.ips.mapper;

import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.internation.controller.internment.dto.internmentstate.DiagnosesGeneralStateDto;
import net.pladema.internation.controller.ips.dto.DiagnosisDto;
import net.pladema.internation.controller.ips.dto.HealthConditionDto;
import net.pladema.internation.controller.ips.dto.HealthHistoryConditionDto;
import net.pladema.internation.repository.ips.generalstate.HealthConditionVo;
import net.pladema.internation.service.ips.domain.DiagnosisBo;
import net.pladema.internation.service.ips.domain.HealthConditionBo;
import net.pladema.internation.service.ips.domain.HealthHistoryConditionBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface HealthConditionMapper {

    @Named("toHealthHistoryConditionDto")
    HealthHistoryConditionDto toHealthHistoryConditionDto(HealthHistoryConditionBo healthConditionBo);

    @Named("toDiagnosisDto")
    DiagnosisDto toDiagnosisDto(DiagnosisBo diagnosisBo);

    @Named("toDiagnosesGeneralStateDto")
   DiagnosesGeneralStateDto toDiagnosesGeneralStateDto(HealthConditionBo healthConditionBo);
}
