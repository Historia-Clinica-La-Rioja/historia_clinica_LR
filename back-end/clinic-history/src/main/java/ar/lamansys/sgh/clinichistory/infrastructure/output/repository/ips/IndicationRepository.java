package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Indication;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IndicationRepository extends SGXAuditableEntityJPARepository<Indication, Integer> {

	@Transactional(readOnly=true)
	@Query(value="SELECT i.typeId "
			+ "FROM Indication i "
			+ "WHERE i.id = :indicationId")
	Optional<Short> getTypeById(@Param("indicationId")Integer indicationId);

	@Transactional
	@Modifying
	@Query( "UPDATE Indication AS i " +
			"SET i.statusId = :statusId, " +
			"i.updateable.updatedOn = CURRENT_TIMESTAMP, " +
			"i.updateable.updatedBy = :userId " +
			"WHERE i.id = :indicationId ")
	void updateStatus(@Param("indicationId") Integer indicationId,
					  @Param("statusId") short statusId,
					  @Param("userId") Integer userId);

}
