package ar.lamansys.sgx.auth.user.infrastructure.output.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

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
	
}
