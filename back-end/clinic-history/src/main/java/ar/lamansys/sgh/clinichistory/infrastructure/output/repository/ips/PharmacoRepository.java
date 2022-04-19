
package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Pharmaco;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PharmacoRepository extends JpaRepository<Pharmaco, Integer> {
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT p "
			+ "FROM Pharmaco p "
			+ "JOIN Indication i ON p.id = i.id "
			+ "JOIN DocumentIndication di ON di.pk.indicationId = i.id "
			+ "JOIN Document doc ON di.pk.documentId = doc.id "
			+ "WHERE doc.sourceId = :internmentEpisodeId "
			+ "AND doc.typeId = :documentTypeId "
			+ "ORDER BY i.creationable.createdOn DESC")
	List<Pharmaco> getByInternmentEpisodeId(@Param("internmentEpisodeId") Integer internmentEpisodeId,
												   @Param("documentTypeId") Short documentTypeId);
}
