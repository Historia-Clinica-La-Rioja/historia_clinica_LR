package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.mapper;

import ar.lamansys.sgh.clinichistory.domain.hce.*;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.*;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class})
public interface HCEGeneralStateMapper {

    @Named("toHCEPersonalHistoryDto")
    HCEPersonalHistoryDto toHCEPersonalHistoryDto(HCEPersonalHistoryBo source);

    @Named("toListHCEPersonalHistoryDto")
    @IterableMapping(qualifiedByName = "toHCEPersonalHistoryDto")
    List<HCEPersonalHistoryDto> toListHCEPersonalHistoryDto(List<HCEPersonalHistoryBo> sourceList);

    @Named("toHCELast2VitalSignsDto")
    HCELast2VitalSignsDto toHCELast2VitalSignsDto(Last2HCEVitalSignsBo resultService);

    @Named("toHCEAnthropometricDataDto")
    HCEAnthropometricDataDto toHCEAnthropometricDataDto(HCEAnthropometricDataBo resultService);

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
}
