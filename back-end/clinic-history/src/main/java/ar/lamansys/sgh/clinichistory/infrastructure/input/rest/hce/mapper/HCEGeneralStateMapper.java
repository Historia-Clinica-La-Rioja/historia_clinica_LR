package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.mapper;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEAllergyBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEHospitalizationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEMedicationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEPersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEToothRecordBo;
import ar.lamansys.sgh.clinichistory.domain.hce.Last2HCERiskFactorsBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.EvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEAllergyDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEAnthropometricDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDiagnoseDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEEvolutionSummaryDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCELast2RiskFactorsDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEMedicationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEPersonalHistoryDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEToothRecordDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface HCEGeneralStateMapper {

    @Named("toHCEPersonalHistoryDto")
    HCEPersonalHistoryDto toHCEPersonalHistoryDto(HCEPersonalHistoryBo source);

    @Named("toListHCEPersonalHistoryDto")
    @IterableMapping(qualifiedByName = "toHCEPersonalHistoryDto")
    List<HCEPersonalHistoryDto> toListHCEPersonalHistoryDto(List<HCEPersonalHistoryBo> sourceList);

    @Named("toHCELast2RiskFactorsDto")
	HCELast2RiskFactorsDto toHCELast2RiskFactorsDto(Last2HCERiskFactorsBo resultService);

    @Named("toHCEAnthropometricDataDto")
    HCEAnthropometricDataDto toHCEAnthropometricDataDto(HCEAnthropometricDataBo resultService);

	@Named("toListHCEAnthropometricDataDto")
	@IterableMapping(qualifiedByName = "toHCEAnthropometricDataDto")
	List<HCEAnthropometricDataDto> toListHCEAnthropometricDataDto(List<HCEAnthropometricDataBo> resultService);

    @Named("toHCEMedicationDto")
    HCEMedicationDto toHCEMedicationDto(HCEMedicationBo source);

    @Named("toListHCEMedicationDto")
    @IterableMapping(qualifiedByName = "toHCEMedicationDto")
    List<HCEMedicationDto> toListHCEMedicationDto(List<HCEMedicationBo> sourceList);

    @Named("toHCEAllergyDto")
    HCEAllergyDto toHCEAllergyDto(HCEAllergyBo source);

    @Named("toListHCEAllergyDto")
    @IterableMapping(qualifiedByName = "toHCEAllergyDto")
    List<HCEAllergyDto> toListHCEAllergyDto(List<HCEAllergyBo> sourceList);

    @Named("toHCEDiagnoseDto")
    HCEDiagnoseDto toHCEDiagnoseDto(HCEHospitalizationBo source);

    @Named("toListHCEToothRecordDto")
    @IterableMapping(qualifiedByName = "toHCEToothRecordDto")
    List<HCEToothRecordDto> toListHCEToothRecordDto(List<HCEToothRecordBo> sourceList);

    @Named("toHCEToothRecordDto")
    @Mapping(target = "date", source = "performedDate")
    HCEToothRecordDto toHCEToothRecordDto(HCEToothRecordBo source);

    @Named("fromOutpatientEvolutionSummaryBo")
    HCEEvolutionSummaryDto fromOutpatientEvolutionSummaryBo(EvolutionSummaryBo evolutionSummaryBo);

    @Named("fromListOutpatientEvolutionSummaryBo")
    @IterableMapping(qualifiedByName = "fromOutpatientEvolutionSummaryBo")
    List<HCEEvolutionSummaryDto> fromListOutpatientEvolutionSummaryBo(List<EvolutionSummaryBo> evolutionSummaryBos);

}
