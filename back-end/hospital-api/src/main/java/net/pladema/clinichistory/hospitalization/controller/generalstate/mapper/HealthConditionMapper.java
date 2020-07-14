package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import net.pladema.clinichistory.hospitalization.controller.dto.internmentstate.DiagnosesGeneralStateDto;
import net.pladema.clinichistory.hospitalization.controller.maindiagnoses.dto.MainDiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.DiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.HealthHistoryConditionDto;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.clinichistory.ips.service.domain.DiagnosisBo;
import net.pladema.clinichistory.ips.service.domain.HealthConditionBo;
import net.pladema.clinichistory.ips.service.domain.HealthHistoryConditionBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
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

    @Named("fromMainDiagnoseDto")
    MainDiagnosisBo fromMainDiagnoseDto(MainDiagnosisDto mainDiagnoses);
}
