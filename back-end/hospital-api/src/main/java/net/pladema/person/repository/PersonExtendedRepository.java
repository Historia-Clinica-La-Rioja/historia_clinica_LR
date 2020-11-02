package net.pladema.person.repository;

import net.pladema.person.repository.entity.PersonExtended;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PersonExtendedRepository extends JpaRepository<PersonExtended, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT pe.photoFilePath " +
            "FROM PersonExtended as pe " +
            "WHERE pe.id = :personId ")
    Optional<String> getPhotoFilePath(@Param("personId") Integer personId);

}
