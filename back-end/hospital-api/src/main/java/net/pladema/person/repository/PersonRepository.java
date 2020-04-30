package net.pladema.person.repository;

import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT pa.id FROM Patient as pa " +
            "JOIN Person as pe ON (pe.id = pa.personId) " +
            "WHERE pe.identificationTypeId = :identificationTypeId " +
            "AND pe.identificationNumber = :identificationNumber " +
            "AND pe.genderId = :genderId")
    List<Integer> findByDniAndGender(@Param("identificationTypeId") Short identificationTypeId, @Param("identificationNumber") String identificationNumber,
                                                @Param("genderId") Short genderId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.person.repository.domain.PersonalInformation(" +
            "p.id, p.identificationNumber, p.birthDate, pe.email," +
            "it.id as identificationTypeId, it.description as identificationTypeDescription, " +
            "pe.cuil, pe.phoneNumber,  " +
            "a.id as addressId, a.street, a.number, a.floor, a.apartment, " +
            "c.id as cityId, c.description as city, " +
            "pr.id as provinceId, pr.description as province)" +
            "FROM Person as p " +
            "JOIN PersonExtended as pe ON (pe.id = p.id) " +
            "JOIN Address as a ON (a.id = pe.addressId) " +
            "JOIN IdentificationType as it ON (it.id = p.identificationTypeId) " +
            "JOIN City as c ON (c.id = a.cityId) " +
            "JOIN Department as d ON (d.id = c.departmentId) " +
            "JOIN Province as pr ON (p.id = d.provinceId) " +
            "WHERE p.id = :personId ")
    Optional<PersonalInformation> getPersonalInformation(@Param("personId") Integer personId);
}