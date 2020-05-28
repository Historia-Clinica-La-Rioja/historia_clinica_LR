package net.pladema.internation.service.documents.searchdocument;

import net.pladema.internation.controller.documents.searchdocument.dto.DocumentSearchFilterDto;
import net.pladema.internation.service.documents.searchdocument.domain.DocumentHistoricBo;

public interface DocumentSearchService {

    DocumentHistoricBo historicalListDocuments(Integer internmentEpisodeId, DocumentSearchFilterDto searchFilter);
}
