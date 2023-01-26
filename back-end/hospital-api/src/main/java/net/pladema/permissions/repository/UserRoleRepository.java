package net.pladema.permissions.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.service.dto.RoleAssignment;

@Repository
public interface UserRoleRepository extends SGXAuditableEntityJPARepository<UserRole, Long> {

	// @formatter:off
	@Transactional(readOnly = true)
	@Query("SELECT ur " +
			"FROM UserRole as ur " +
			"WHERE ur.userId = :userId " +
			"AND ur.deleteable.deleted = FALSE")
	List<UserRole> findByUserId(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query("SELECT ur " +
			"FROM UserRole as ur " +
			"WHERE ur.userId = :userId " +
			"AND ur.deleteable.deleted = TRUE")
	List<UserRole> findDeletedByUserId(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query("SELECT ur FROM UserRole as ur WHERE ur.institutionId = :institutionId " +
			"AND ur.deleteable.deleted = FALSE")
	List<UserRole> findByInstitutionId(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT ur FROM UserRole as ur WHERE ur.userId = :userId " +
			"AND ur.roleId = :roleId " +
			"AND ur.institutionId = :institutionId " +
			"AND ur.deleteable.deleted = TRUE")
	Optional<UserRole> getUserRoleIfIsDeleted(@Param("userId") Integer userId,
											  @Param("roleId") Short roleId,
											  @Param("institutionId") Integer institutionId);

	@Transactional
	@Modifying
	@Query("DELETE FROM UserRole as ur "
			+ "WHERE ur.userId = :userId ")
	void deleteByUserId(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.permissions.service.dto.RoleAssignment( " +
			"	ur.roleId, " +
			"	ur.institutionId" +
			" )"
			+ "FROM UserRole ur "
			+ "WHERE ur.userId = :userId "
			+ "AND ur.deleteable.deleted = FALSE"
			)
	List<RoleAssignment> getRoleAssignments(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query("SELECT ur.userId "+
			"FROM UserRole ur " +
			"WHERE ur.roleId IN :rolesId " +
			"AND ur.deleteable.deleted = FALSE")
	List<Integer> findAllByRoles(@Param("rolesId") List<Short> rolesId);


	@Transactional(readOnly = true)
	@Query("SELECT ur " +
			"FROM UserRole as ur " +
			"WHERE ur.userId = :userId " +
			"AND ur.roleId = :roleId " +
			"AND ur.deleteable.deleted = FALSE")
	Optional<UserRole> findByRoleAndUserId(@Param("userId") Integer userId, @Param("roleId") Short roleId);


	@Transactional(readOnly = true)
	@Query("SELECT ur " +
			"FROM UserRole as ur " +
			"WHERE ur.userId = :userId " +
			"AND ur.roleId = :roleId " +
			"AND ur.institutionId = :institutionId " +
			"AND ur.deleteable.deleted = FALSE")
	Optional<UserRole> findByRoleInstitutionAndUserId(@Param("userId") Integer userId,
													  @Param("roleId") Short roleId,
													  @Param("institutionId") Integer institutionId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE UserRole ur " +
			"SET ur.deleteable.deleted = false " +
			"WHERE ur.userId = :userId AND ur.roleId = :roleId AND ur.institutionId = :institutionId")
	void setDeletedFalse(@Param("userId") Integer userId, @Param("roleId") Short roleId, @Param("institutionId") Integer institutionId);

	// @formatter:on
}
