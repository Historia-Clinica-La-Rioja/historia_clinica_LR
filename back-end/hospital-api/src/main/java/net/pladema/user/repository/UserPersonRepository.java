package net.pladema.user.repository;

import net.pladema.user.repository.entity.UserPerson;
import net.pladema.user.repository.entity.UserPersonPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPersonRepository extends JpaRepository<UserPerson, UserPersonPK> {

	@Query("SELECT (case when count(up.pk.userId)> 0 then true else false end) "
			+ "FROM UserPerson up "
			+ "WHERE up.pk.personId = :personId")
	boolean existsByPersonId(@Param("personId") Integer personId);

	@Query("SELECT up.pk.userId FROM UserPerson up WHERE up.pk.personId = :personId")
	Optional<Integer> getUserIdByPersonId(@Param("personId") Integer personId);

	@Query("SELECT up.pk.personId FROM UserPerson up WHERE up.pk.userId = :userId")
	Optional<Integer> getPersonIdByUserId(@Param("userId") Integer userId);

	@Query("SELECT up FROM UserPerson up WHERE up.pk.userId = :userId")
    Optional<UserPerson> getByUserId(@Param("userId") Integer userId);
}