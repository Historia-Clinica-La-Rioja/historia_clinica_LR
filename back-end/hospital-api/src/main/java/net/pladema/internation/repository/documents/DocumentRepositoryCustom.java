package net.pladema.internation.repository.documents;

import net.pladema.internation.repository.documents.searchdocument.DocumentSearchQuery;
import net.pladema.internation.repository.documents.searchdocument.DocumentSearchVo;

import java.util.List;

public interface DocumentRepositoryCustom {

    <T extends DocumentSearchQuery> List<DocumentSearchVo> historicSearch(Integer internmentEpisodeId, T structuredQuery);

}
