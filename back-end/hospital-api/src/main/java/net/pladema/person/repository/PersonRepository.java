package net.pladema.person.repository;

import net.pladema.person.repository.domain.CompleteDataPerson;
import net.pladema.person.repository.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.person.repository.domain.CompleteDataPerson(p, e, a) FROM Person as p " +
            "JOIN PersonExtended as e ON (e.id = p.id) " +
            "JOIN Address as a ON (a.id = e.addressId) " +
            "WHERE p.identificationTypeId = :identificationTypeId " +
            "AND p.identificationNumber = :identificationNumber " +
            "AND p.genderId = :genderId")
    List<CompleteDataPerson> findByDniAndGender(@Param("identificationTypeId") Short identificationTypeId, @Param("identificationNumber") String identificationNumber,
                                                @Param("genderId") Short genderId);
}