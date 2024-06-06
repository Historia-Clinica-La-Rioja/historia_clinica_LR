package net.pladema.person.repository;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import net.pladema.person.repository.domain.PersonPhotoVo;
import net.pladema.person.repository.entity.PersonExtended;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PersonExtendedRepository extends JpaRepository<PersonExtended, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT pe.photoFilePath " +
            "FROM PersonExtended as pe " +
            "WHERE pe.id = :personId ")
    Optional<String> getPhotoFilePath(@Param("personId") Integer personId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.person.repository.domain.PersonPhotoVo(pe.id, pe.photoFilePath) " +
            "FROM PersonExtended as pe " +
            "WHERE pe.id IN :personIds ")
    List<PersonPhotoVo> getPhotoFilePath(@Param("personIds") List<Integer> personIds);

	@Transactional(readOnly = true)
	@Query("SELECT pe.otherGenderSelfDetermination " +
			"FROM PersonExtended as pe " +
			"WHERE pe.id = :personId ")
	Optional<String> getOtherSelfPerceivedGender(@Param("personId") Integer personId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW ar.lamansys.sgh.shared.domain.general.ContactInfoBo(pe.phonePrefix, pe.phoneNumber, a.street, a.number, a.floor, a.apartment, c.description," +
			"p.description, pe.email) " +
			"FROM PersonExtended pe " +
			"JOIN Address a ON (a.id = pe.addressId) " +
			"LEFT JOIN City c ON (c.id = a.cityId) " +
			"LEFT JOIN Province p ON (p.id = a.provinceId) " +
			"WHERE pe.id = :personId")
	ContactInfoBo getContactInfoById(@Param("personId") Integer personId);
	
	@Transactional
	@Modifying
	@Query("UPDATE PersonExtended pex " +
			"SET pex.email = :email " +
			"WHERE pex.id = :id ")
	void setEmail(@Param("email") String email, @Param("id") Integer id);

}
