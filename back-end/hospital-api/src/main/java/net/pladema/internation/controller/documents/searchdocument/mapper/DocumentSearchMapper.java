package net.pladema.internation.controller.documents.searchdocument.mapper;

import net.pladema.internation.controller.documents.searchdocument.dto.DocumentHistoricDto;
import net.pladema.internation.service.documents.searchdocument.domain.DocumentHistoricBo;
import org.mapstruct.Mapper;

@Mapper
public interface DocumentSearchMapper {

    DocumentHistoricDto toDocumentHistoricDto(DocumentHistoricBo documentHistoricBo);

}
