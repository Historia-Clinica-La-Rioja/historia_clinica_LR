package net.pladema.person.repository;

import net.pladema.person.repository.domain.PersonPhotoVo;
import net.pladema.person.repository.entity.PersonExtended;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

}
