package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto.DocumentHistoricDto;
import ar.lamansys.sgh.clinichistory.domain.document.search.DocumentHistoricBo;
import org.mapstruct.Mapper;
@Mapper(uses = {LocalDateMapper.class})
public interface DocumentSearchMapper {

    DocumentHistoricDto toDocumentHistoricDto(DocumentHistoricBo documentHistoricBo);

}
