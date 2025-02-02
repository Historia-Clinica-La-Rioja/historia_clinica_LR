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

@Slf4j
@RequiredArgsConstructor
@Service
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
        log.debug("Input parameters -> ecl {}", ecl);
        List<SnomedSearchItemVo> items = searchConcepts(term, ecl, groupDescription);
        Integer matchCount = Math.toIntExact(getTotalResultCount(term, ecl, groupDescription));
        SnomedSearchVo result = new SnomedSearchVo(items, matchCount);
        log.debug("Output -> {}", result);
        return result;
    }

    private List<SnomedSearchItemVo> searchConcepts(String term, String ecl, String groupDescription) {
        String sqlString = term != null ?
                "SELECT s.id, s.sctid, s.pt " +
                    ", ts_rank( to_tsvector('spanish', s.pt), plainto_tsquery('spanish', :term), 2 ) as rank " +
						// the parameter '2' makes the ts_rank function divide the rank by the document length
						// there are other modes documented in PostgreSQL's doc "Controlling Text Search"
                "FROM Snomed s " +
                "JOIN SnomedRelatedGroup srg ON (s.id = srg.snomedId) " +
                "JOIN SnomedGroup sg ON (srg.groupId = sg.id) " +
                "WHERE fts(s.pt, :term) = true " +
                    "AND (sg.ecl = :ecl) " +
					"AND (sg.description = :groupDescription) " +
                    "AND (srg.lastUpdate >= sg.lastUpdate) " +
                "ORDER BY rank DESC "

				:

				"SELECT s.id, s.sctid, s.pt " +
						", ts_rank( to_tsvector('spanish', s.pt), '0') as rank " +
						// in this case, there's no term to filter, so the rank is set to 0
						" FROM Snomed s " +
						" JOIN SnomedRelatedGroup srg ON (s.id = srg.snomedId) " +
						" JOIN SnomedGroup sg ON (srg.groupId = sg.id) " +
						" WHERE (sg.ecl = :ecl) " +
						" AND (sg.description = :groupDescription) " +
						" AND (srg.lastUpdate >= sg.lastUpdate) " +
						" ORDER BY rank DESC "
        ;

        Query query = term != null ? entityManager.createQuery(sqlString)
				.setParameter("ecl", ecl)
				.setParameter("groupDescription", groupDescription)
                .setParameter("term", term)
                .setMaxResults(LIMIT)
				:
				entityManager.createQuery(sqlString)
						.setParameter("ecl", ecl)
						.setParameter("groupDescription", groupDescription)
						.setMaxResults(LIMIT)
                ;

        List<Object[]> queryResult = query.getResultList();


        List<SnomedSearchItemVo> result = queryResult.stream()
				.map(o -> new SnomedSearchItemVo((Integer) o[0], (String) o[1], (String) o[2]))
				.collect(Collectors.toList());

		List<Integer> conceptIds = result.stream()
				.map(SnomedSearchItemVo::getSnomedId)
				.collect(Collectors.toList());

		List<SnomedSearchItemVo> synonyms = getConceptsSynonyms(conceptIds);

		result.addAll(synonyms);

        return result.stream().distinct().collect(Collectors.toList());
    }

    private Long getTotalResultCount(String term, String ecl, String groupDescription) {

        String sqlString = term != null ?
                "SELECT COUNT(s.id) " +
                "FROM Snomed s " +
                "JOIN SnomedRelatedGroup srg ON (s.id = srg.snomedId) " +
                "JOIN SnomedGroup sg ON (srg.groupId = sg.id)  " +
                "WHERE fts(s.pt, :term) = true " +
					"AND (sg.ecl = :ecl) " +
					"AND (sg.description = :groupDescription ) " +
                    "AND (srg.lastUpdate >= sg.lastUpdate ) "
                 :
				"SELECT COUNT(s.id) " +
						"FROM Snomed s " +
						"JOIN SnomedRelatedGroup srg ON (s.id = srg.snomedId) " +
						"JOIN SnomedGroup sg ON (srg.groupId = sg.id)  " +
						"WHERE (sg.ecl = :ecl) " +
						"AND (sg.description = :groupDescription ) " +
						"AND (srg.lastUpdate >= sg.lastUpdate ) ";

        Query query = term != null ?
				entityManager.createQuery(sqlString)
				.setParameter("ecl", ecl)
				.setParameter("groupDescription", groupDescription)
                .setParameter("term", term)
				:
				entityManager.createQuery(sqlString)
						.setParameter("ecl", ecl)
						.setParameter("groupDescription", groupDescription);

        List<Object> queryResult = query.getResultList();

        return (Long) queryResult.get(0);

    }
	
	private List<SnomedSearchItemVo> getConceptsSynonyms(List<Integer> conceptIds){
		String sqlString = 
				"SELECT s.id, s.sctid, s.pt " +
				"FROM Snomed s " +
				"JOIN SnomedSynonym ss ON (s.id = ss.pk.synonymId OR s.id = ss.pk.mainConceptId) " +
				"JOIN SnomedRelatedGroup srg ON (s.id = srg.snomedId) " +
				"JOIN SnomedGroup sg ON (srg.groupId = sg.id) " +
				"WHERE ((ss.pk.mainConceptId IN (:conceptIds) and s.synonym = true) " +
						"OR (ss.pk.synonymId IN (:conceptIds) AND s.synonym = false)) " +
						"AND (srg.lastUpdate >= sg.lastUpdate) " +
				"GROUP BY s.id"
				;
		
		Query query = entityManager.createQuery(sqlString)
				.setParameter("conceptIds", conceptIds)
				;

		List<Object[]> queryResult = query.getResultList();

		List<SnomedSearchItemVo> result = queryResult.stream()
				.map(o -> new SnomedSearchItemVo((Integer) o[0], (String) o[1], (String) o[2]))
				.collect(Collectors.toList());
		return result;
	} 

}
