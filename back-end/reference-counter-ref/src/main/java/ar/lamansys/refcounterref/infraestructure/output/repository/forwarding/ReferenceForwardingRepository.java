package ar.lamansys.refcounterref.infraestructure.output.repository.forwarding;

import ar.lamansys.refcounterref.domain.reference.ReferenceForwardingBo;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReferenceForwardingRepository extends SGXAuditableEntityJPARepository<ReferenceForwarding, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceForwardingBo(rf.id, up.pk.personId, " +
			"rf.creationable.createdBy, rf.observation, rf.forwardingTypeId, rf.creationable.createdOn) " +
			"FROM ReferenceForwarding rf " +
			"JOIN UserPerson up ON (rf.creationable.createdBy = up.pk.userId)" +
			"WHERE rf.reference_id = :referenceId " +
			"ORDER BY rf.creationable.createdOn DESC")
	List<ReferenceForwardingBo> findByReferenceId(@Param("referenceId") Integer referenceId);

}
