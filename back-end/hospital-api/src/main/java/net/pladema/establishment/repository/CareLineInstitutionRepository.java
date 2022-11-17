package net.pladema.establishment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.establishment.repository.entity.CareLineInstitution;

@Repository
public interface CareLineInstitutionRepository extends JpaRepository<CareLineInstitution, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT cli " +
			"FROM CareLineInstitution cli " +
			"WHERE cli.institutionId = :institutionId " +
			"AND cli.careLineId = :careLineId " +
			"AND cli.deleted = false ")
	Optional<CareLineInstitution> findByInstitutionIdAndCareLineId(@Param("institutionId") Integer institutionId,
																   @Param("careLineId") Integer careLineId);

	@Modifying
	@Query("UPDATE CareLineInstitution cli SET cli.deleted = true WHERE cli.id = :id")
	void deleteById(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query(value = "SELECT cli.id " +
			"FROM CareLineInstitution cli " +
			"WHERE cli.institutionId IN :institutionsIds ")
	List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);
}
