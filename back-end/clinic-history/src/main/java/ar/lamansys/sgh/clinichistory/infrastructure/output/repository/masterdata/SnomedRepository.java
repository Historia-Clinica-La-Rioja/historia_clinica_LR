package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Repository
public interface SnomedRepository extends JpaRepository<Snomed, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT s.id " +
            "FROM Snomed s " +
            "WHERE s.sctid = :sctid " +
            "AND s.pt = :pt ")
    Optional<Integer> findIdBySctidAndPt(@Param("sctid") String sctid, @Param("pt") String pt);

    @Transactional(readOnly = true)
    @Query("SELECT MAX(s.id) " +
            "FROM Snomed s " +
            "WHERE s.sctid = :sctid ")
    Optional<Integer> findLatestIdBySctid(@Param("sctid") String sctid);

	@Query("SELECT sg.id " +
			"FROM SnomedGroup sg " +
			"WHERE sg.description LIKE :description")
	Integer getIdFromGroup(@Param("description") String description);

	@Query("SELECT s " +
			"FROM SnomedGroup sg JOIN SnomedRelatedGroup srg ON (sg.id = srg.groupId) " +
			"JOIN Snomed s ON (s.id = srg.snomedId) " +
			"WHERE sg.groupId = :parentId " +
			"AND fts(s.pt, :term ) = TRUE " +
			"AND sg.institutionId = :institutionId")
	List<Snomed> getVademecumConcepts(@Param("term") String term,
									  @Param("parentId") Integer parentId,
									  @Param("institutionId") Integer institutionId,
									  Pageable pageable);

	@Query("SELECT COUNT(s) " +
			"FROM SnomedGroup sg JOIN SnomedRelatedGroup srg ON (sg.id = srg.groupId) " +
			"JOIN Snomed s ON (s.id = srg.snomedId) " +
			"WHERE sg.groupId = :parentId " +
			"AND fts(s.pt, :term ) = TRUE " +
			"AND sg.institutionId = :institutionId")
	Long getTotalConcepts(@Param("term") String term,
									  @Param("parentId") Integer parentId,
									  @Param("institutionId") Integer institutionId);
}
