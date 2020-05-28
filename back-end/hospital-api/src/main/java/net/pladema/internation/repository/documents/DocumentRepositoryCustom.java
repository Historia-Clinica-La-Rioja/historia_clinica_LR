package net.pladema.internation.repository.documents;

import net.pladema.internation.repository.documents.search.DocumentSearchQuery;
import net.pladema.internation.repository.documents.search.DocumentSearchVo;

import java.util.List;

public interface DocumentRepositoryCustom {

    <T extends DocumentSearchQuery> List<DocumentSearchVo> historicSearch(Integer internmentEpisodeId, T structuredQuery);

}
