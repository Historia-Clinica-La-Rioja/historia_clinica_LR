package net.pladema.person.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.person.repository.domain.CompletePersonVo;
import net.pladema.person.repository.domain.PersonRecipientVo;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT pa.id FROM Patient as pa " +
            "JOIN Person as pe ON (pe.id = pa.personId) " +
            "WHERE pe.identificationTypeId = :identificationTypeId " +
            "AND pe.identificationNumber = :identificationNumber " +
            "AND pe.genderId = :genderId")
    List<Integer> findByDniAndGender(
            @Param("identificationTypeId") Short identificationTypeId,
            @Param("identificationNumber") String identificationNumber,
            @Param("genderId") Short genderId
    );

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.person.repository.domain.PersonalInformation(" +
            "p.id, p.identificationNumber, p.birthDate, pe.email," +
            "it.id as identificationTypeId, it.description as identificationTypeDescription, " +
            "pe.cuil, pe.phonePrefix, pe.phoneNumber,  " +
            "a.id as addressId, a.street, a.number, a.floor, a.apartment, " +
            "c.id as cityId, c.description as city, " +
            "pr.id as provinceId, pr.description as province)" +
            "FROM Person as p " +
            "LEFT JOIN PersonExtended as pe ON (pe.id = p.id) " +
            "LEFT JOIN Address as a ON (a.id = pe.addressId) " +
            "LEFT JOIN IdentificationType as it ON (it.id = p.identificationTypeId) " +
            "LEFT JOIN City as c ON (c.id = a.cityId) " +
            "LEFT JOIN Department as d ON (d.id = c.departmentId) " +
            "LEFT JOIN Province as pr ON (pr.id = d.provinceId) " +
            "WHERE p.id = :personId ")
    Optional<PersonalInformation> getPersonalInformation(@Param("personId") Integer personId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.person.repository.domain.CompletePersonVo( p, pe, a, c, pr, d, co) " +
            "FROM Person as p " +
            "LEFT JOIN PersonExtended as pe ON (pe.id = p.id) " +
            "LEFT JOIN Address as a ON (a.id = pe.addressId) " +
            "LEFT JOIN City as c ON (c.id = a.cityId) " +
            "LEFT JOIN Department as d ON (d.id = a.departmentId) " +
            "LEFT JOIN Province as pr ON (pr.id = a.provinceId) " +
			"LEFT JOIN Country as co ON (co.id = a.countryId) " +
            "WHERE p.id = :personId ")
    Optional<CompletePersonVo> getCompletePerson(@Param("personId") Integer personId);

    @Transactional(readOnly = true)
    @Query("SELECT p " +
    "FROM Diary d " +
    "JOIN HealthcareProfessional hp ON d.healthcareProfessionalId = hp.id " +
    "JOIN Person p ON hp.personId = p.id " +
    "WHERE d.id = :diaryId")
    Optional<Person> findProfessionalNameByDiaryId(@Param("diaryId") Integer diaryId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.person.repository.domain.PersonRecipientVo(" +
			"p.firstName, " +
			"p.lastName, " +
			"pe.nameSelfDetermination, " +
			"pe.phonePrefix, " +
			"pe.phoneNumber, " +
			"pe.email " +
			") FROM Person as p " +
			"LEFT JOIN PersonExtended as pe ON (pe.id = p.id) " +
			"WHERE p.id = :personId ")
	Optional<PersonRecipientVo> getPersonRecipient(@Param("personId") Integer personId);

}
