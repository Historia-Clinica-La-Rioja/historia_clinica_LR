package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.domain.InstitutionalGroupInstitutionVo;
import net.pladema.establishment.repository.entity.InstitutionalGroupInstitution;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InstitutionalGroupInstitutionRepository extends SGXAuditableEntityJPARepository<InstitutionalGroupInstitution, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.establishment.repository.domain.InstitutionalGroupInstitutionVo(igi.id, igi.institutionId, igi.institutionalGroupId, i.name, d.description) " +
			"FROM InstitutionalGroupInstitution igi " +
			"JOIN Institution i ON (i.id = igi.institutionId) " +
			"JOIN Address a ON (a.id = i.addressId) " +
			"LEFT JOIN City c ON (a.cityId = c.id) " +
			"LEFT JOIN Department d ON (c.departmentId = d.id) " +
			"WHERE igi.institutionalGroupId = :institutionalGroupId " +
			"AND igi.deleteable.deleted = FALSE ")
	List<InstitutionalGroupInstitutionVo> findByInstitutionalGroupId(@Param("institutionalGroupId") Integer institutionalGroupId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE InstitutionalGroupInstitution igi "
			+ "SET igi.deleteable.deleted = true "
			+ ", igi.deleteable.deletedOn = CURRENT_TIMESTAMP "
			+ ", igi.deleteable.deletedBy = ?#{ principal.userId } "
			+ "WHERE igi.institutionalGroupId = :institutionalGroupId" )
	void deleteByInstitutionalGroupId(@Param("institutionalGroupId") Integer institutionalGroupId);

	@Transactional(readOnly = true)
	@Query("SELECT case when count(igi) > 0 then TRUE else FALSE END " +
			"FROM InstitutionalGroupInstitution igi " +
			"WHERE igi.institutionId = :institutionId " +
			"AND igi.deleteable.deleted = FALSE ")
	boolean existsByInstitutionId (@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT igi.institutionId " +
			"FROM InstitutionalGroupInstitution igi " +
			"WHERE igi.institutionalGroupId = :institutionalGroupId " +
			"AND igi.deleteable.deleted = FALSE")
	List<Integer> findInstitutionIdsByInstitutionalGroupId(@Param("institutionalGroupId")Integer institutionalGroupId);


	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT igi.institutionId " +
			"FROM InstitutionalGroupInstitution igi " +
			"JOIN InstitutionalGroup ig ON (ig.id = igi.institutionalGroupId) " +
			"JOIN InstitutionalGroupUser igu ON (igu.institutionalGroupId = ig.id) " +
			"WHERE igu.userId = :userId " +
			"AND igu.deleteable.deleted = FALSE " +
			"AND igi.deleteable.deleted = FALSE")
	List<Integer> findInstitutionIdsByUserId(@Param("userId") Integer userId);

}
