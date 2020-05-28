package net.pladema.internation.repository.documents;

import net.pladema.internation.repository.documents.search.DocumentSearchQuery;
import net.pladema.internation.repository.documents.search.DocumentSearchVo;
import net.pladema.sgx.repository.QueryPart;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class DocumentRepositoryImpl implements DocumentRepositoryCustom {

    private final EntityManager entityManager;

    public DocumentRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DocumentSearchQuery> List<DocumentSearchVo> historicSearch(Integer internmentEpisodeId, T structuredQuery) {
        QueryPart queryPart = new QueryPart(
                "SELECT ")
                .concatPart(structuredQuery.select())
                .concat(" FROM ")
                .concatPart(structuredQuery.from())
                .concat("WHERE ")
                .concatPart(structuredQuery.where())
                .concat("ORDER BY ")
                .concatPart(structuredQuery.orderBy())
                .addParam("internmentEpisodeId", internmentEpisodeId);
        Query query = entityManager.createQuery(queryPart.toString());
        queryPart.configParams(query);
        return structuredQuery.construct(query.getResultList());
    }
}
