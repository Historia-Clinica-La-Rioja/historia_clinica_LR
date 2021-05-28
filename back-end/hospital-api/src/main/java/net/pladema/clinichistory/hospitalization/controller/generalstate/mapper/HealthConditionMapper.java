package net.pladema.clinichistory.hospitalization.controller.generalstate.mapper;

import jdk.jfr.Name;
import net.pladema.clinichistory.hospitalization.controller.dto.internmentstate.DiagnosesGeneralStateDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.DiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.HealthHistoryConditionDto;
import net.pladema.clinichistory.hospitalization.controller.maindiagnoses.dto.MainDiagnosisDto;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.clinichistory.documents.controller.dto.HealthConditionNewConsultationDto;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionNewConsultationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthHistoryConditionBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface HealthConditionMapper {

    @Named("toHealthHistoryConditionDto")
    HealthHistoryConditionDto toHealthHistoryConditionDto(HealthHistoryConditionBo healthConditionBo);

    @Named("toDiagnosisDto")
    DiagnosisDto toDiagnosisDto(DiagnosisBo diagnosisBo);

    @Named("toDiagnosesGeneralStateDto")
    DiagnosesGeneralStateDto toDiagnosesGeneralStateDto(HealthConditionBo healthConditionBo);

    @Named("fromMainDiagnoseDto")
    MainDiagnosisBo fromMainDiagnoseDto(MainDiagnosisDto mainDiagnoses);

    @Name("toHealthConditionNewConsultationDto")
    HealthConditionNewConsultationDto toHealthConditionNewConsultationDto(HealthConditionNewConsultationBo bo);

}
