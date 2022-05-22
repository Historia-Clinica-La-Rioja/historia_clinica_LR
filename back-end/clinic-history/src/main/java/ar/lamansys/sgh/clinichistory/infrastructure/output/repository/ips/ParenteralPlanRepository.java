package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.ParenteralPlan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ParenteralPlanRepository extends JpaRepository<ParenteralPlan, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT pp "
			+ "FROM ParenteralPlan pp "
			+ "JOIN Indication i ON i.id = pp.id "
			+ "JOIN DocumentIndication di ON di.pk.indicationId = i.id "
			+ "JOIN Document doc ON di.pk.documentId = doc.id "
			+ "WHERE doc.sourceId = :internmentEpisodeId "
			+ "AND doc.typeId = :documentTypeId "
			+ "ORDER BY i.creationable.createdOn DESC")
	List<ParenteralPlan> getByInternmentEpisodeId(@Param("internmentEpisodeId") Integer internmentEpisodeId,
												   @Param("documentTypeId") Short documentTypeId);

}
