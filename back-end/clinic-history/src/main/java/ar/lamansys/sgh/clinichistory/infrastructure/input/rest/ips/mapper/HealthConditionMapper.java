package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.*;
import jdk.jfr.Name;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionNewConsultationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthHistoryConditionBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface HealthConditionMapper {

    @Named("toHealthHistoryConditionDto")
    HealthHistoryConditionDto toHealthHistoryConditionDto(HealthHistoryConditionBo healthConditionBo);

    @Named("toHealthHistoryConditionBo")
    HealthHistoryConditionBo toHealthHistoryConditionBo(HealthHistoryConditionDto healthHistoryConditionDto);

    @Named("toListHealthHistoryConditionBo")
    @IterableMapping(qualifiedByName = "toHealthHistoryConditionBo")
    List<HealthHistoryConditionBo> toListHealthHistoryConditionBo(List<HealthHistoryConditionDto> healthHistoryConditionDto);

    @Named("toDiagnosisDto")
    DiagnosisDto toDiagnosisDto(DiagnosisBo diagnosisBo);

    @Named("toDiagnosesGeneralStateDto")
    DiagnosesGeneralStateDto toDiagnosesGeneralStateDto(HealthConditionBo healthConditionBo);

    @Name("toHealthConditionNewConsultationDto")
    HealthConditionNewConsultationDto toHealthConditionNewConsultationDto(HealthConditionNewConsultationBo bo);

}
