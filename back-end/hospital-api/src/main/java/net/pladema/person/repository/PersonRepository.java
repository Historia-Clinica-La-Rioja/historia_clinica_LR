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
    @Query("SELECT pa.id FROM Patient as pa " +
            "JOIN Person as pe ON (pe.id = pa.personId) " +
            "WHERE pe.identificationTypeId = :identificationTypeId " +
            "AND pe.identificationNumber = :identificationNumber " +
            "AND pe.genderId = :genderId")
    List<Integer> findByDniAndGender(@Param("identificationTypeId") Short identificationTypeId, @Param("identificationNumber") String identificationNumber,
                                                @Param("genderId") Short genderId);
}