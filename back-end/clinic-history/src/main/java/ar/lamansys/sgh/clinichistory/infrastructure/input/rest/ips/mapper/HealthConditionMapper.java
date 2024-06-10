package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FamilyHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionNewConsultationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosesGeneralStateDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionNewConsultationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthHistoryConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.PersonalHistoryDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.ReferableItemDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface HealthConditionMapper {

    @Named("toPersonalHistoryBo")
    PersonalHistoryBo toPersonalHistoryBo(HealthHistoryConditionDto healthHistoryConditionDto);

    @Named("toFamilyHistoryBo")
    FamilyHistoryBo toFamilyHistoryBo(HealthHistoryConditionDto healthHistoryConditionDto);

    @Named("toListFamilyHistoryBoFromHealthHistory")
    @IterableMapping(qualifiedByName = "toFamilyHistoryBo")
    List<FamilyHistoryBo> toListFamilyHistoryBoFromHealthHistory(List<HealthHistoryConditionDto> healthHistoryConditionDto);

    @Named("toHealthHistoryConditionDto")
    HealthHistoryConditionDto toHealthHistoryConditionDto(PersonalHistoryBo personalHistory);

    @Named("toHealthHistoryConditionDto")
    HealthHistoryConditionDto toHealthHistoryConditionDto(FamilyHistoryBo familyHistory);

    @Named("toListDiagnosisDto")
    @IterableMapping(qualifiedByName = "toDiagnosisDto")
    List<DiagnosisDto> toListDiagnosisDto(List<DiagnosisBo> diagnosisBo);

    @Named("toDiagnosisDto")
    DiagnosisDto toDiagnosisDto(DiagnosisBo diagnosisBo);

    @Named("toListDiagnosisBo")
    @IterableMapping(qualifiedByName = "toDiagnosisBo")
    List<DiagnosisBo> toListDiagnosisBo(List<DiagnosisDto> diagnosisDto);

    @Named("toDiagnosisBo")
    DiagnosisBo toDiagnosisBo(DiagnosisDto diagnosisDto);

    @Named("toHealthConditionBoFromDiagnosisDto")
    HealthConditionBo toHealthConditionBoFromDiagnosisDto(DiagnosisDto diagnosisDto);

    @Named("toDiagnosisDtoFromHealthConditionBo")
    DiagnosisDto toDiagnosisDtoFromHealthConditionBo(HealthConditionBo healthConditionBo);

    @Named("toDiagnosesGeneralStateDto")
    DiagnosesGeneralStateDto toDiagnosesGeneralStateDto(HealthConditionBo healthConditionBo);

    @Named("toHealthConditionNewConsultationDto")
    HealthConditionNewConsultationDto toHealthConditionNewConsultationDto(HealthConditionNewConsultationBo bo);

    @Named("toListPersonalHistoryBoFromPersonalHistoryDto")
    @IterableMapping(qualifiedByName = "toPersonalHistoryBo")
    List<PersonalHistoryBo> toListPersonalHistoryBoFromPersonalHistoryDto(List<PersonalHistoryDto> personalHistories);

    @Named("toPersonalHistoryDto")
    PersonalHistoryDto toPersonalHistoryDto(PersonalHistoryBo personalHistory);

    @Named("toPersonalHistoryBo")
    PersonalHistoryBo toPersonalHistoryBo(PersonalHistoryDto personalHistory);

    @Named("toHealthConditionDto")
    HealthConditionDto toHealthConditionDto(HealthConditionBo healthConditionBo);

    @Named("toListHealthConditionDto")
    @IterableMapping(qualifiedByName = "toHealthConditionDto")
    List<HealthConditionDto> toListHealthConditionDto(List<HealthConditionBo> healthConditions);

    @Named("toHealthConditionBo")
    HealthConditionBo toHealthConditionBo(HealthConditionDto healthConditionDto);

    @Named("toListHealthConditionBo")
    @IterableMapping(qualifiedByName = "toHealthConditionBo")
    List<HealthConditionBo> toListHealthConditionBo(List<HealthConditionDto> healthConditions);

	@Named("toReferablePersonalHistoryBoFromPersonalHistoryDto")
	ReferableItemBo<PersonalHistoryBo> toReferablePersonalHistoryBoFromPersonalHistoryDto(ReferableItemDto<PersonalHistoryDto> personalHistories);

	@Named("toReferablePersonalHistoryDto")
	ReferableItemDto<PersonalHistoryDto> toReferablePersonalHistoryDto(ReferableItemBo<PersonalHistoryBo> personalHistory);

	@Named("toReferableFamilyHistoryBoFromHealthHistory")
	ReferableItemBo<FamilyHistoryBo> toReferableFamilyHistoryBoFromHealthHistory(ReferableItemDto<HealthHistoryConditionDto> healthHistoryConditionDto);

	@Named("toReferableFamilyHistoryConditionDto")
	ReferableItemDto<HealthHistoryConditionDto> toReferableFamilyHistoryConditionDto(ReferableItemBo<FamilyHistoryBo> familyHistory);

}
