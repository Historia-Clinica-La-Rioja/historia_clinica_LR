package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentSearchQuery;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentSearchVo;

import java.util.List;

public interface DocumentRepositoryCustom {

    <T extends DocumentSearchQuery> List<DocumentSearchVo> doHistoricSearch(Integer internmentEpisodeId, T structuredQuery);

}
