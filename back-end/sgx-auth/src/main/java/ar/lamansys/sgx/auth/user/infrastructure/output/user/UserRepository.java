package ar.lamansys.sgx.auth.user.infrastructure.output.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	@Modifying
	@Query("UPDATE User AS u SET u.twoFactorAuthenticationSecret = :secret WHERE u.id = :id")
	void setTwoFactorAuthenticationSecret(@Param("id") Integer id, @Param("secret") String secret);

	@Query("SELECT u.twoFactorAuthenticationSecret FROM User u WHERE u.id = :id")
	Optional<String> getTwoFactorAuthenticationSecret(@Param("id") Integer id);

	@Transactional
	@Modifying
	@Query("UPDATE User AS u SET u.twoFactorAuthenticationSecret = NULL, u.twoFactorAuthenticationEnabled = false WHERE u.id = :id")
	void resetTwoFactorAuthentication(@Param("id") Integer id);

	@Transactional
	@Modifying
	@Query("UPDATE User AS u SET u.twoFactorAuthenticationEnabled = true WHERE u.id = :id")
	void enableTwoFactorAuthentication(@Param("id") Integer id);

	@Query("SELECT u.twoFactorAuthenticationEnabled FROM User AS u WHERE u.id = :id")
	Boolean userHasTwoFactorAuthenticationEnabled(@Param("id") Integer id);
	
}
