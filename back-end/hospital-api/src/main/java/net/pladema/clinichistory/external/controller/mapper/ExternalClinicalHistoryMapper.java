package net.pladema.clinichistory.external.controller.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.external.controller.dto.ExternalClinicalHistorySummaryDto;
import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistorySummaryBo;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface ExternalClinicalHistoryMapper {

    @Named("fromExternalClinicalHistorySummaryBo")
    ExternalClinicalHistorySummaryDto fromExternalClinicalHistorySummaryBo(ExternalClinicalHistorySummaryBo externalClinicalHistorySummaryBo);

    @Named("fromListExternalClinicalHistorySummaryBo")
    @IterableMapping(qualifiedByName = "fromExternalClinicalHistorySummaryBo")
    List<ExternalClinicalHistorySummaryDto> fromListExternalClinicalHistorySummaryBo(List<ExternalClinicalHistorySummaryBo> externalClinicalHistorySummaryBos);
}
