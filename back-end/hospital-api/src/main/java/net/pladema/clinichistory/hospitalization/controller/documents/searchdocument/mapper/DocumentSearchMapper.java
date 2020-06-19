package net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.mapper;

import net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.dto.DocumentHistoricDto;
import net.pladema.clinichistory.documents.service.searchdocument.domain.DocumentHistoricBo;
import org.mapstruct.Mapper;

@Mapper
public interface DocumentSearchMapper {

    DocumentHistoricDto toDocumentHistoricDto(DocumentHistoricBo documentHistoricBo);

}
