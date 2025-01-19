package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.establishment.repository.entity.CareLineRole;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CareLineRoleRepository extends SGXAuditableEntityJPARepository<CareLineRole, Integer> {

	@Transactional(readOnly = true)
	@Query(" SELECT COUNT(1) > 0 " +
			"FROM CareLineRole clr " +
			"WHERE clr.careLineId = :careLineId " +
			"AND clr.roleId = :roleId " +
			"AND clr.deleteable.deleted IS FALSE")
	Boolean alreadyExistsCareLineAssignedRole(@Param("careLineId") Integer careLineId, @Param("roleId") Short roleId);

	@Transactional(readOnly = true)
	@Query(" SELECT COUNT(1) > 0 " +
			"FROM CareLineRole clr " +
			"WHERE clr.careLineId = :careLineId " +
			"AND clr.deleteable.deleted IS FALSE")
	Boolean careLineHasAssignedRoles(@Param("careLineId") Integer careLineId);

}
