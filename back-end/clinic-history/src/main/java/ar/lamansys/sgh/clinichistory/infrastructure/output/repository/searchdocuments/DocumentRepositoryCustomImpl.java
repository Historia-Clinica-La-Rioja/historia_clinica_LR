package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;


import ar.lamansys.sgx.shared.repositories.QueryPart;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class DocumentRepositoryCustomImpl implements DocumentRepositoryCustom {

    private final EntityManager entityManager;

    public DocumentRepositoryCustomImpl(EntityManager entityManager){
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
