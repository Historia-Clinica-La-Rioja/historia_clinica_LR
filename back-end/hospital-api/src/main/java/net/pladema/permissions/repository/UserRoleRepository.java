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
			"WHERE ur.userId = :userId ")
	List<UserRole> findByUserId(@Param("userId") Integer userId);

	@Transactional
	@Modifying
	@Query("DELETE UserRole as ur "
			+ "WHERE ur.userId = :userId ")
	void deleteByUserId(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.permissions.service.dto.RoleAssignment( " +
			"	ur.roleId, " +
			"	ur.institutionId" +
			" )"
			+ "FROM UserRole ur "
			+ "WHERE ur.userId = :userId "
			+ "AND ur.deleteable.deleted = false"
			)
	List<RoleAssignment> getRoleAssignments(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query("SELECT ur.userId "+
			"FROM UserRole ur " +
			"WHERE ur.roleId IN :rolesId ")
	List<Integer> findAllByRoles(@Param("rolesId") List<Short> rolesId);


	@Transactional(readOnly = true)
	@Query("SELECT ur " +
			"FROM UserRole as ur " +
			"WHERE ur.userId = :userId " +
			"AND ur.roleId = :roleId ")
	Optional<UserRole> findByRoleAndUserId(@Param("userId") Integer userId, @Param("roleId") Short roleId);


	@Transactional(readOnly = true)
	@Query("SELECT ur " +
			"FROM UserRole as ur " +
			"WHERE ur.userId = :userId " +
			"AND ur.roleId = :roleId " +
			"AND ur.institutionId = :institutionId")
	Optional<UserRole> findByRoleInstitutionAndUserId(@Param("userId") Integer userId,
													  @Param("roleId") Short roleId,
													  @Param("institutionId") Integer institutionId);

	// @formatter:on
}
