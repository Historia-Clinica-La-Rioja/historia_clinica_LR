package ar.lamansys.sgh.clinichistory.application.searchDocument;


import ar.lamansys.sgh.clinichistory.application.searchDocument.domain.DocumentSearchFilterBo;

public interface DocumentSearchService {

    DocumentHistoricBo getListHistoricalDocuments(Integer internmentEpisodeId, DocumentSearchFilterBo searchFilter);

}
