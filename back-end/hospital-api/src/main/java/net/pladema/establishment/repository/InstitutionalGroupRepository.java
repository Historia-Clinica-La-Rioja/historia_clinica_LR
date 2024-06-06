package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.InstitutionalGroup;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InstitutionalGroupRepository extends SGXAuditableEntityJPARepository<InstitutionalGroup, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT i.name " +
			"FROM InstitutionalGroup ig " +
			"JOIN InstitutionalGroupInstitution igi ON (ig.id = igi.institutionalGroupId) " +
			"JOIN Institution i ON (i.id = igi.institutionId) " +
			"WHERE ig.id = :id " +
			"AND ig.deleteable.deleted = FALSE " +
			"AND igi.deleteable.deleted = FALSE")
	List<String> getInstitutionsNamesById(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query("SELECT case when count(ig) > 0 then TRUE else FALSE END " +
			"FROM InstitutionalGroup ig " +
			"WHERE ig.typeId = :typeId " +
			"AND ig.name = :name " +
			"AND ig.deleteable.deleted = FALSE")
	boolean existsByTypeIdAndName(@Param("typeId") Short typeId, @Param("name") String name);

	@Transactional(readOnly = true)
	@Query("SELECT case when count(ig) > 0 then TRUE else FALSE END " +
			"FROM InstitutionalGroup ig " +
			"JOIN InstitutionalGroupUser igu ON (ig.id = igu.institutionalGroupId) " +
			"WHERE ig.id = :id " +
			"AND igu.deleteable.deleted = FALSE")
	boolean existsUsersByGroupId (@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query("SELECT ig " +
			"FROM InstitutionalGroup ig " +
			"JOIN InstitutionalGroupUser igu ON (igu.institutionalGroupId = ig.id) " +
			"WHERE igu.userId = :userId " +
			"AND ig.deleteable.deleted IS FALSE " +
			"AND igu.deleteable.deleted IS FALSE " +
			"ORDER BY ig.name ASC")
	List<InstitutionalGroup> getInstitutionalGroupByUserId(@Param("userId")Integer userId);

}
