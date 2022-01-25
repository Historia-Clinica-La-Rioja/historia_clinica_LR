package net.pladema.clinichistory.external.controller.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.external.controller.dto.ExternalClinicalHistoryDto;
import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistoryBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface ExternalClinicalHistoryMapper {

    @Named("fromExternalClinicalHistoryBo")
    ExternalClinicalHistoryDto fromExternalClinicalHistoryBo(ExternalClinicalHistoryBo externalClinicalHistoryBo);

    @Named("fromListExternalClinicalHistoryBo")
    @IterableMapping(qualifiedByName = "fromExternalClinicalHistoryBo")
    List<ExternalClinicalHistoryDto> fromListExternalClinicalHistoryBo(List<ExternalClinicalHistoryBo> externalClinicalHistoryBos);
}
