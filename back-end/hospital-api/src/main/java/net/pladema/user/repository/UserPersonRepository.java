package net.pladema.user.repository;

import net.pladema.person.repository.domain.InstitutionUserPersonBo;
import net.pladema.person.repository.domain.ManagerUserPersonBo;
import net.pladema.user.repository.entity.UserPerson;
import net.pladema.user.repository.entity.UserPersonPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

	@Query("SELECT DISTINCT NEW net.pladema.person.repository.domain.InstitutionUserPersonBo(ur.institutionId, up.pk.userId, p.id, p.firstName, " +
			"p.middleNames, p.lastName, p.otherLastNames, p.identificationNumber) " +
			"FROM User u " +
			"JOIN UserPerson up ON (up.pk.userId = u.id) " +
			"JOIN UserRole ur ON (up.pk.userId = ur.userId) " +
			"JOIN Person p ON (up.pk.personId = p.id) " +
			"WHERE ur.institutionId = :institutionId " +
			"AND ur.deleteable.deleted IS FALSE " +
			"AND u.enable = TRUE " +
			"AND u.deleteable.deleted = FALSE")
	List<InstitutionUserPersonBo> findAllByInstitutionIdAndUserIds(@Param("institutionId") Integer institutionId);

	@Query("SELECT DISTINCT NEW net.pladema.person.repository.domain.InstitutionUserPersonBo(up.pk.userId, p.id, p.firstName, " +
			"p.middleNames, p.lastName, p.otherLastNames, p.identificationNumber) " +
			"FROM UserPerson up " +
			"JOIN Person p ON (up.pk.personId = p.id) " +
			"JOIN UserRole ur ON (up.pk.userId = ur.userId) " +
			"WHERE up.pk.userId IN (:userIds)")
	List<InstitutionUserPersonBo> findByUserIds(@Param("userIds") List<Integer> userIds);

	@Query("SELECT DISTINCT NEW net.pladema.person.repository.domain.ManagerUserPersonBo(up.pk.userId, p.id, p.firstName, " +
			"p.middleNames, p.lastName, p.otherLastNames, p.identificationNumber) " +
			"FROM UserPerson up " +
			"JOIN Person p ON (up.pk.personId = p.id) " +
			"WHERE up.pk.userId IN (:userIds) ")
	List<ManagerUserPersonBo> findAllByUserIds(@Param("userIds") List<Integer> userIds);

	@Query("SELECT pex.cuil " +
			"FROM UserPerson up " +
			"JOIN PersonExtended pex " +
			"ON pex.id = up.pk.personId " +
			"WHERE up.pk.userId = :userId")
	Optional<String> getCuilByUserId(@Param("userId") Integer userId);
}