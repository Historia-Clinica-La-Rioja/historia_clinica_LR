package ar.lamansys.refcounterref.infraestructure.output.repository.referenceobservation;

import ar.lamansys.refcounterref.domain.reference.ReferenceObservationBo;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReferenceObservationRepository extends SGXAuditableEntityJPARepository<ReferenceObservation, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceObservationBo(up.pk.personId, " +
			"ro.observation, ro.creationable.createdOn) " +
			"FROM ReferenceObservation ro " +
			"JOIN UserPerson up ON (ro.creationable.createdBy = up.pk.userId) " +
			"WHERE ro.reference_id = :referenceId " +
			"ORDER BY ro.creationable.createdOn DESC")
	List<ReferenceObservationBo> getObservationsByReferenceId(@Param("referenceId") Integer referenceId);

}
