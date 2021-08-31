package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;


import java.util.List;

public interface DocumentRepositoryCustom {

    <T extends DocumentSearchQuery> List<DocumentSearchVo> historicSearch(Integer internmentEpisodeId, T structuredQuery);

}
