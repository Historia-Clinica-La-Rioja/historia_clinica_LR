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
			"	r.description, " +
			"	ur.institutionId" +
			" )"
			+ "FROM UserRole ur "
			+ "JOIN Role r ON (r.id = ur.userRolePK.roleId) "
			+ "WHERE ur.userRolePK.userId = :userId "
			)
	List<RoleAssignment> getRoleAssignments(@Param("userId") Integer userId);

	// @formatter:on
}
