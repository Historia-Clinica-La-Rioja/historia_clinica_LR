package net.pladema.clinichistory.documents.service.searchdocument;

import net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.dto.DocumentSearchFilterDto;
import net.pladema.clinichistory.documents.service.searchdocument.domain.DocumentHistoricBo;

public interface DocumentSearchService {

    DocumentHistoricBo historicalListDocuments(Integer internmentEpisodeId, DocumentSearchFilterDto searchFilter);
}
