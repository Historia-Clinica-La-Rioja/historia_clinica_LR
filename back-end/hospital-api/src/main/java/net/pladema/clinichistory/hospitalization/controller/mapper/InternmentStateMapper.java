package net.pladema.clinichistory.hospitalization.controller.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.HospitalizationGeneralState;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthHistoryConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.Last2RiskFactorsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AllergyConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnthropometricDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosesGeneralStateDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthHistoryConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ImmunizationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.Last2RiskFactorsDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.MedicationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AllergyConditionMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.AnthropometricDataMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.HealthConditionMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.ImmunizationMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.MedicationMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentGeneralStateDto;

@Mapper(uses = {HealthConditionMapper.class, RiskFactorMapper.class, AnthropometricDataMapper.class,
        MedicationMapper.class, ImmunizationMapper.class, AllergyConditionMapper.class})
public interface InternmentStateMapper {

    @Named("toListDiagnosisDto")
    @IterableMapping(qualifiedByName = "toDiagnosisDto")
    List<DiagnosisDto> toListDiagnosisDto(List<DiagnosisBo> listDiagnosisBo);

    @Named("toListHealthHistoryConditionDto")
    @IterableMapping(qualifiedByName = "toHealthHistoryConditionDto")
    List<HealthHistoryConditionDto> toListHealthHistoryConditionDto(List<HealthHistoryConditionBo> listHealthHistoryCondition);

    @Named("toListDiagnosesGeneralStateDto")
    @IterableMapping(qualifiedByName = "toDiagnosesGeneralStateDto")
    List<DiagnosesGeneralStateDto> toListDiagnosesGeneralStateDto(List<HealthConditionBo> diagnoses);

    @Named("toListMedicationDto")
    @IterableMapping(qualifiedByName = "toMedicationDto")
    List<MedicationDto> toListMedicationDto(List<MedicationBo> listMedicationBo);

    @Named("toListImmunizationDto")
    @IterableMapping(qualifiedByName = "toImmunizationDto")
    List<ImmunizationDto> toListImmunizationDto(List<ImmunizationBo> immunizationBos);
    
    @Named("toListAllergyConditionDto")
    @IterableMapping(qualifiedByName = "toAllergyConditionDto")
    List<AllergyConditionDto> toListAllergyConditionDto(List<AllergyConditionBo> allergyConditionBos);

    @Named("toInternmentGeneralStateDto")
    @Mapping(target = "diagnosis", source = "diagnosis", qualifiedByName = "toListDiagnosisDto")
    @Mapping(target = "personalHistories", source = "personalHistories", qualifiedByName = "toListHealthHistoryConditionDto")
    @Mapping(target = "familyHistories", source = "familyHistories", qualifiedByName = "toListHealthHistoryConditionDto")
    @Mapping(target = "riskFactors", source = "riskFactors")
    @Mapping(target = "medications", source = "medications", qualifiedByName = "toListMedicationDto")
    InternmentGeneralStateDto toInternmentGeneralStateDto(HospitalizationGeneralState interment);

    @Named("toAnthropometricDataDto")
    AnthropometricDataDto toAnthropometricDataDto(AnthropometricDataBo anthropometricData);

	@Named("toListAnthropometricDataDto")
	@IterableMapping(qualifiedByName = "toAnthropometricDataDto")
	List<AnthropometricDataDto> toListAnthropometricDataDto(List<AnthropometricDataBo> anthropometricData);

	@Named("toHealthConditionDto")
    HealthConditionDto toHealthConditionDto(HealthConditionBo mainDiagnosis);

    @Named("toLast2RiskFactorDto")
	Last2RiskFactorsDto toLast2RiskFactorDto(Last2RiskFactorsBo riskFactorBos);
}
