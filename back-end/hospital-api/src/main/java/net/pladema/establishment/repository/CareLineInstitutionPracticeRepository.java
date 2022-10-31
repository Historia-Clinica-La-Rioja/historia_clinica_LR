package net.pladema.establishment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.establishment.repository.entity.CareLineInstitutionPractice;

@Repository
public interface CareLineInstitutionPracticeRepository extends JpaRepository<CareLineInstitutionPractice, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT clip " +
			"FROM CareLineInstitutionPractice clip " +
			"WHERE clip.careLineInstitutionId = :careLineInstitutionId " +
			"AND clip.snomedRelatedGroupId = :snomedRelatedGroupId ")
	Optional<CareLineInstitutionPractice> findByCareLineInstitutionIdAndSnomedRelatedGroupId(@Param("careLineInstitutionId") Integer careLineInstitutionId,
																							 @Param("snomedRelatedGroupId") Integer snomedRelatedGroupId);

	@Transactional(readOnly = true)
	@Query("SELECT clip " +
			"FROM CareLineInstitutionPractice clip " +
			"WHERE clip.snomedRelatedGroupId = :snomedRelatedGroupId ")
	List<CareLineInstitutionPractice> findBySnomedRelatedGroupId(@Param("snomedRelatedGroupId") Integer snomedRelatedGroupId);

}
