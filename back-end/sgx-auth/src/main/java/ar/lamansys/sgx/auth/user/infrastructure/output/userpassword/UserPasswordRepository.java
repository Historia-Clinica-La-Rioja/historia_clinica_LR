package ar.lamansys.sgx.auth.user.infrastructure.output.userpassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserPasswordRepository extends JpaRepository<UserPassword, Integer> {

	@Query("SELECT up.password FROM UserPassword up WHERE up.id = :id ORDER BY up.creationable.createdOn DESC")
	Optional<String> getPasswordById(@Param("id") Integer id);

	@Transactional
	@Modifying
	@Query("UPDATE UserPassword AS up SET up.password = :password, up.updateable.updatedOn = :today WHERE up.id = :userId")
	void updatePassword(@Param("userId") Integer userId, @Param("password") String password,
			@Param("today") LocalDateTime today);

	@Query("SELECT COUNT(up.id) FROM UserPassword up WHERE up.id = :id AND up.updateable.updatedOn > :tokenDate")
	int passwordUpdateAfter(@Param("id") Integer id, @Param("tokenDate") LocalDateTime tokenDate);

}
