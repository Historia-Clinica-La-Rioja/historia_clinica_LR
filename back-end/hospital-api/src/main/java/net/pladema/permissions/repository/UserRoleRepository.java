package net.pladema.permissions.repository;

import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.entity.UserRolePK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {

	// @formatter:off
	@Transactional(readOnly = true)
	@Query("SELECT ur FROM UserRole as ur WHERE ur.userRolePK.userId = :userId")
	Optional<UserRole> findByUserId(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query("SELECT r.description "
			+ "FROM UserRole ur "
			+ "JOIN Role r ON (r.id = ur.userRolePK.roleId) "
			+ "WHERE ur.userRolePK.userId = :userId "
			+ "ORDER BY ur.audit.createdOn DESC")
	Page<String> getActiveRole(@Param("userId") Integer userId, Pageable page);
	
	@Transactional
	@Modifying
	@Query("UPDATE UserRole as ur "
			+ "SET ur.userRolePK.roleId = :roleId "
			+ "WHERE ur.userRolePK.userId = :userId ")
	void updateRole(@Param("userId") Integer userId, @Param("roleId") Short roleId);

	@Transactional
	@Modifying
	@Query("DELETE UserRole as ur "
			+ "WHERE ur.userRolePK.userId = :userId ")
	void deleteByUserId(@Param("userId") Integer userId);
	
	// @formatter:on

}
