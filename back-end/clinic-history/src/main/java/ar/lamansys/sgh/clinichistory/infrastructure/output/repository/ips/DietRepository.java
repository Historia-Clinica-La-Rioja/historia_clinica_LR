package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Diet;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

@Repository
public interface DietRepository extends SGXAuditableEntityJPARepository<Diet, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT d "
			+ "FROM Diet d "
			+ "JOIN Indication i ON i.id = d.id "
			+ "JOIN DocumentIndication di ON di.pk.indicationId = i.id "
			+ "JOIN Document doc ON di.pk.documentId = doc.id "
			+ "WHERE doc.sourceId = :internmentEpisodeId "
			+ "AND doc.typeId = 12")
	List<Diet> getByInternmentEpisodeId(@Param("internmentEpisodeId") Integer internmentEpisodeId);

}
