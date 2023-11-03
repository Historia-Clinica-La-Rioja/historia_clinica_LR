package ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserKeyRepository extends JpaRepository<UserKey, Integer> {

	@Query("SELECT uk " +
			"FROM UserKey uk " +
			"WHERE uk.key = :key")
	Optional<UserKey> getUserKeyByKey(@Param("key") String key);

	@Query("SELECT uk " +
			"FROM UserKey uk " +
			"WHERE uk.userId = :userId")
	List<UserKey> findUserKeyByUser(@Param("userId") Integer userId);

	void deleteByNameAndUserId(String name, Integer userId);
}
