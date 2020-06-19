package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.searchdocument.DocumentSearchQuery;
import net.pladema.clinichistory.documents.repository.searchdocument.DocumentSearchVo;

import java.util.List;

public interface DocumentRepositoryCustom {

    <T extends DocumentSearchQuery> List<DocumentSearchVo> historicSearch(Integer internmentEpisodeId, T structuredQuery);

}
