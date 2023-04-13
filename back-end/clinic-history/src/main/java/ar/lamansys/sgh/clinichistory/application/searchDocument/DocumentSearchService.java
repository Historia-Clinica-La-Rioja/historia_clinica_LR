package ar.lamansys.sgh.clinichistory.application.searchDocument;


import ar.lamansys.sgh.clinichistory.application.searchDocument.domain.DocumentSearchFilterBo;

import java.util.List;

public interface DocumentSearchService {

    DocumentHistoricBo historicalListDocuments(Integer internmentEpisodeId, DocumentSearchFilterBo searchFilter);

}
