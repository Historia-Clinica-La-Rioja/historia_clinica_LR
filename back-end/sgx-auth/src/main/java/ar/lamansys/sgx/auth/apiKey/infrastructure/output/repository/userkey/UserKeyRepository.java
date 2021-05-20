package ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserKeyRepository extends JpaRepository<UserKey, Integer> {

	@Query("SELECT uk " +
			"FROM UserKey uk " +
			"WHERE uk.pk.key = :key")
	Optional<UserKey> getUserKeyByKey(@Param("key") String key);

}
