package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentSearchQuery;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain.DocumentSearchVo;
import ar.lamansys.sgx.shared.repositories.QueryPart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DocumentRepositoryCustomImpl implements DocumentRepositoryCustom {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public <T extends DocumentSearchQuery> List<DocumentSearchVo> doHistoricSearch(Integer internmentEpisodeId, T structuredQuery) {
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
