package net.pladema.user.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.user.repository.entity.User;

@Repository
public interface UserRepository extends SGXAuditableEntityJPARepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByUsername(@Param("username") String username);

	@Query("SELECT u.id FROM User u WHERE u.username = :username")
	Optional<Integer> getUserId(@Param("username") String username);

	@Query("SELECT u.id FROM User u WHERE u.personId = :personId")
	Optional<Integer> getUserIdByPersonId(@Param("personId") Integer personId);

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
	
	@Query("SELECT (case when count(u.id)> 0 then true else false end) "
			+ "FROM User u "
			+ "WHERE u.username = :username")
	boolean existByUsername(@Param("username") String username);

	@Query("SELECT (case when count(u.id)> 0 then true else false end) "
			+ "FROM User u "
			+ "WHERE u.personId = :personId")
	boolean existsByPersonId(@Param("personId") Integer personId);

}
