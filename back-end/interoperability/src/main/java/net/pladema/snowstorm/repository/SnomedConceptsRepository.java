package net.pladema.snowstorm.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.repository.domain.SnomedSearchItemVo;
import net.pladema.snowstorm.repository.domain.SnomedSearchVo;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnomedConceptsRepository {

    private static final Integer LIMIT = 30;
    private final EntityManager entityManager;

	public SnomedSearchVo searchConceptsByEcl(String term, String ecl, String groupDescription) {
		log.debug("Input parameters -> term {}, ecl {}", term, ecl);
		List<SnomedSearchItemVo> items = searchConcepts(term, ecl, groupDescription);
		SnomedSearchVo result = new SnomedSearchVo(items);
		log.debug("Output -> {}", result);
		return result;
	}

    public SnomedSearchVo searchConceptsWithResultCountByEcl(String term, String ecl, String groupDescription) {
        log.debug("Input parameters -> term {}, ecl {}", term, ecl);
        List<SnomedSearchItemVo> items = searchConcepts(term, ecl, groupDescription);
        Integer matchCount = Math.toIntExact(getTotalResultCount(term, ecl, groupDescription));
        SnomedSearchVo result = new SnomedSearchVo(items, matchCount);
        log.debug("Output -> {}", result);
        return result;
    }

    private List<SnomedSearchItemVo> searchConcepts(String term, String ecl, String groupDescription) {
        String sqlString =
                "SELECT s.id, s.sctid, s.pt " +
                    ", ts_rank( to_tsvector('spanish', s.pt), plainto_tsquery('spanish', :term), 2 ) as rank " +
						// the parameter '2' makes the ts_rank function divide the rank by the document length
						// there are other modes documented in PostgreSQL's doc "Controlling Text Search"
                "FROM Snomed s " +
                "JOIN SnomedRelatedGroup srg ON (s.id = srg.snomedId) " +
                "JOIN SnomedGroup sg ON (srg.groupId = sg.id)  " +
                "WHERE fts(s.pt, :term ) = true " +
                    "AND (sg.ecl = :ecl ) " +
					"AND (sg.description = :groupDescription ) " +
                    "AND (srg.lastUpdate >= sg.lastUpdate ) " +
                "ORDER BY rank DESC "
        ;

        Query query = entityManager.createQuery(sqlString)
				.setParameter("ecl", ecl)
				.setParameter("groupDescription", groupDescription)
                .setParameter("term", term)
                .setMaxResults(LIMIT)
                ;

        List<Object[]> queryResult = query.getResultList();

        List<SnomedSearchItemVo> result = queryResult.stream()
                .map(o -> new SnomedSearchItemVo((Integer) o[0], (String) o[1], (String) o[2]))
                .collect(Collectors.toList());
        return result;
    }

    private Long getTotalResultCount(String term, String ecl, String groupDescription) {

        String sqlString =
                "SELECT COUNT(s.id) " +
                "FROM Snomed s " +
                "JOIN SnomedRelatedGroup srg ON (s.id = srg.snomedId) " +
                "JOIN SnomedGroup sg ON (srg.groupId = sg.id)  " +
                "WHERE fts(s.pt, :term ) = true " +
					"AND (sg.ecl = :ecl ) " +
					"AND (sg.description = :groupDescription ) " +
                    "AND (srg.lastUpdate >= sg.lastUpdate ) "
                ;

        Query query = entityManager.createQuery(sqlString)
				.setParameter("ecl", ecl)
				.setParameter("groupDescription", groupDescription)
                .setParameter("term", term)
                ;

        List<Object> queryResult = query.getResultList();

        return (Long) queryResult.get(0);

    }

}
