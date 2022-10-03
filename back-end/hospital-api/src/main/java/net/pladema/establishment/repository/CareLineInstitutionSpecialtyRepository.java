package net.pladema.establishment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.establishment.repository.entity.CareLineInstitutionSpecialty;

@Repository
public interface CareLineInstitutionSpecialtyRepository extends JpaRepository<CareLineInstitutionSpecialty, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT clis " +
			"FROM CareLineInstitutionSpecialty clis " +
			"WHERE clis.careLineInstitutionId = :careLineInstitutionId " +
			"AND clis.clinicalSpecialtyId = :clinicalSpecialtyId ")
	Optional<CareLineInstitutionSpecialty> findByCareLineInstitutionIdAndClinicalSpecialtyId(@Param("careLineInstitutionId") Integer careLineInstitutionId,
																							 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

	@Transactional(readOnly = true)
	@Query("SELECT clis " +
			"FROM CareLineInstitutionSpecialty clis " +
			"JOIN CareLineInstitution cli ON (clis.careLineInstitutionId = cli.id) " +
			"WHERE clis.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND cli.careLineId = :careLineId " +
			"AND cli.institutionId = :institutionId " +
			"AND cli.deleted = false ")
	Optional<CareLineInstitutionSpecialty> findByCareLineIdAndInstitutionIdAndClinicalSpecialtyId(@Param("careLineId") Integer careLineId,
																							 @Param("institutionId") Integer institutionId,
																							 @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);
}
