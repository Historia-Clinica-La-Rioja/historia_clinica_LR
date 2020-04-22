package net.pladema.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.user.repository.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByUsername(@Param("username") String username);

	@Query("SELECT u.id FROM User u WHERE u.username = :username")
	Optional<Integer> getUserId(@Param("username") String username);

	@Transactional
	@Modifying
	@Query("UPDATE User AS u SET u.enable = :status WHERE u.id = :id")
	void changeStatusAccount(@Param("id") Integer id, @Param("status") Boolean status);
	
	@Transactional
	@Modifying
	@Query("UPDATE User AS u SET u.lastLogin = :lastLogin WHERE u.id = :id")
	void updateLoginDate(@Param("id") Integer id, @Param("lastLogin") LocalDateTime lastLogin);

	@Query("SELECT (case when count(u.id)> 0 then true else false end) FROM User u "
			+ "WHERE u.username = :username AND u.enable = true")
	boolean isEnable(@Param("username") String username);
	
	@Query("SELECT (case when count(u.id)> 0 then true else false end) FROM User u "
			+ "WHERE u.id = :id AND u.enable = true")
	boolean isEnable(@Param("id") Integer id);

	
	@Query("SELECT u.id as id, u.username as username, "
			+ "u.lastLogin as lastLogin, u.enable as enable, r.description as role "
			+ "FROM User u "
			+ "JOIN UserRole ur ON (ur.userRolePK.userId = u.id)"
			+ "JOIN Role r ON (r.id = ur.userRolePK.roleId) "
			+ "WHERE ur.audit.createdOn IN (  SELECT MAX(ur1.audit.createdOn)"
			+ "                                      	  FROM UserRole ur1"
			+ "                                      	  WHERE ur1.userRolePK.userId = u.id"
			+ "		                                      GROUP BY ur1.userRolePK.userId)")
	<T> Page<T> pageableUsers(Pageable pageable, Class<T> clazz);

	
	@Query("SELECT (case when count(u.id)> 0 then true else false end) "
			+ "FROM User u "
			+ "WHERE u.username = :username")
	boolean existByUsername(@Param("username") String username);

	@Query("SELECT u "
			+ "FROM User u "
			+ "JOIN UserRole ur ON (ur.userRolePK.userId = u.id) "
			+ "WHERE ur.userRolePK.roleId = 1")
	List<User> getAdminUser();
}
