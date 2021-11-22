package net.pladema.permissions.repository;

import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.entity.UserRolePK;
import net.pladema.permissions.service.dto.RoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {

	// @formatter:off
	@Transactional(readOnly = true)
	@Query("SELECT ur FROM UserRole as ur WHERE ur.userRolePK.userId = :userId")
	List<UserRole> findByUserId(@Param("userId") Integer userId);

	@Transactional
	@Modifying
	@Query("DELETE UserRole as ur "
			+ "WHERE ur.userRolePK.userId = :userId ")
	void deleteByUserId(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.permissions.service.dto.RoleAssignment( " +
			"	ur.userRolePK.roleId, " +
			"	ur.userRolePK.institutionId" +
			" )"
			+ "FROM UserRole ur "
			+ "WHERE ur.userRolePK.userId = :userId "
			)
	List<RoleAssignment> getRoleAssignments(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query("SELECT ur.userRolePK.userId "+
			"FROM UserRole ur " +
			"WHERE ur.userRolePK.roleId IN :rolesId ")
	List<Integer> findAllByRoles(@Param("rolesId") List<Short> rolesId);
	// @formatter:on
}
