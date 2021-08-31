package net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.mapper;

import net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.dto.DocumentHistoricDto;
import ar.lamansys.sgh.clinichistory.application.searchDocument.DocumentHistoricBo;
import org.mapstruct.Mapper;

@Mapper
public interface DocumentSearchMapper {

    DocumentHistoricDto toDocumentHistoricDto(DocumentHistoricBo documentHistoricBo);

}
