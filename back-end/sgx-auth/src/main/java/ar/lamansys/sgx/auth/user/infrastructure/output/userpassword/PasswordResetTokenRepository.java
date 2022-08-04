package ar.lamansys.sgx.auth.user.infrastructure.output.userpassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	@Query("SELECT PRT FROM PasswordResetToken PRT " +
			" WHERE PRT.token = :token" +
			" AND PRT.enable = true" +
			" AND PRT.expiryDate > CURRENT_TIMESTAMP")
	Optional<PasswordResetToken> findByToken(@Param("token") String token);

	@Transactional
	@Modifying
	@Query("UPDATE PasswordResetToken AS PRT " +
			" SET PRT.enable = false " +
			" WHERE PRT.userId = :userId")
	void disableTokens(@Param("userId") Integer userId);
}
