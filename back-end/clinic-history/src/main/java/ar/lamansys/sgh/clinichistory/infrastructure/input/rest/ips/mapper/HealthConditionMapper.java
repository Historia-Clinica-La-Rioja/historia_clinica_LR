package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FamilyHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionNewConsultationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosesGeneralStateDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionNewConsultationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthHistoryConditionDto;
import jdk.jfr.Name;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface HealthConditionMapper {

    @Named("toPersonalHistoryBo")
    PersonalHistoryBo toPersonalHistoryBo(HealthHistoryConditionDto healthHistoryConditionDto);

    @Named("toListPersonalHistoryBoFromHealthHistory")
    @IterableMapping(qualifiedByName = "toPersonalHistoryBo")
    List<PersonalHistoryBo> toListPersonalHistoryBoFromHealthHistory(List<HealthHistoryConditionDto> healthHistoryConditionDto);

    @Named("toFamilyHistoryBo")
    FamilyHistoryBo toFamilyHistoryBo(HealthHistoryConditionDto healthHistoryConditionDto);

    @Named("toListFamilyHistoryBoFromHealthHistory")
    @IterableMapping(qualifiedByName = "toFamilyHistoryBo")
    List<FamilyHistoryBo> toListFamilyHistoryBoFromHealthHistory(List<HealthHistoryConditionDto> healthHistoryConditionDto);

    @Named("toHealthHistoryConditionDto")
    HealthHistoryConditionDto toHealthHistoryConditionDto(PersonalHistoryBo personalHistory);

    @Named("toHealthHistoryConditionDto")
    HealthHistoryConditionDto toHealthHistoryConditionDto(FamilyHistoryBo familyHistory);

    @Named("toDiagnosisDto")
    DiagnosisDto toDiagnosisDto(DiagnosisBo diagnosisBo);

    @Named("toDiagnosesGeneralStateDto")
    DiagnosesGeneralStateDto toDiagnosesGeneralStateDto(HealthConditionBo healthConditionBo);

    @Name("toHealthConditionNewConsultationDto")
    HealthConditionNewConsultationDto toHealthConditionNewConsultationDto(HealthConditionNewConsultationBo bo);

}
