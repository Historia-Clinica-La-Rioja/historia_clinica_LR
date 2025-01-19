package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.establishment.repository.entity.InstitutionalGroupUser;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InstitutionalGroupUserRepository extends SGXAuditableEntityJPARepository<InstitutionalGroupUser, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT case when count(igu) > 0 then TRUE else FALSE END " +
			"FROM InstitutionalGroupUser igu " +
			"WHERE igu.institutionalGroupId = :institutionalGroupId " +
			"AND igu.userId = :userId " +
			"AND igu.deleteable.deleted = FALSE")
	boolean existsByInstitutionalGroupIdAndUserId (@Param("institutionalGroupId") Integer institutionalGroupId, @Param("userId") Integer userId);


	@Transactional(readOnly = true)
	@Query("SELECT case when count(igu) > 0 then TRUE else FALSE END " +
			"FROM InstitutionalGroupUser igu " +
			"WHERE igu.userId = :userId " +
			"AND igu.deleteable.deleted = FALSE")
	boolean existsByUserId (@Param("userId") Integer userId);

}
