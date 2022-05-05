package net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.dto.DocumentHistoricDto;
import ar.lamansys.sgh.clinichistory.application.searchDocument.DocumentHistoricBo;
import org.mapstruct.Mapper;

@Mapper(uses = {LocalDateMapper.class})
public interface DocumentSearchMapper {

    DocumentHistoricDto toDocumentHistoricDto(DocumentHistoricBo documentHistoricBo);

}
