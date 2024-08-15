package net.pladema.establishment.infrastructure.output.repository;

import net.pladema.establishment.infrastructure.output.entity.ClinicalSpecialtySector;

import net.pladema.staff.repository.entity.ClinicalSpecialty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicalServiceSectorRepository extends JpaRepository<ClinicalSpecialtySector, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT cs FROM  ClinicalSpecialtySector css "
			+ " INNER JOIN ClinicalSpecialty cs ON cs.id = css.clinicalSpecialtyId "
			+ " INNER JOIN Sector s ON css.sectorId = s.id "
			+ " WHERE css.sectorId = :idSector AND s.institutionId = :institutionId"
			+ " AND cs.clinicalSpecialtyTypeId = 1")
	List<ClinicalSpecialty> getAllBySectorAndInstitution(@Param("idSector") Integer idSector,
														 @Param("institutionId") Integer institutionId);


	@Transactional(readOnly = true)
	@Query(value = " SELECT s.institutionId " +
			"FROM  ClinicalSpecialtySector css " +
			"INNER JOIN Sector s ON (css.sectorId = s.id) " +
			"WHERE css.id = :id")
	Integer getInstitutionId(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query(value = "SELECT css.id " +
			"FROM  ClinicalSpecialtySector css " +
			"INNER JOIN ClinicalSpecialty cs ON (css.clinicalSpecialtyId = cs.id) " +
			"WHERE cs.clinicalSpecialtyTypeId = 1")
	List<Integer> getAllIds();

	@Transactional(readOnly = true)
	@Query(value = " SELECT css.id " +
			"FROM  ClinicalSpecialtySector css " +
			"INNER JOIN Sector s ON (css.sectorId = s.id) " +
			"INNER JOIN ClinicalSpecialty cs ON (css.clinicalSpecialtyId = cs.id) " +
			"WHERE s.institutionId IN :institutionsIds " +
			"AND cs.clinicalSpecialtyTypeId = 1")
	List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);

	Optional<ClinicalSpecialtySector> findByClinicalSpecialtyIdAndSectorId(Integer clinicalSpecialtyId, Integer sectorId);

	Optional<ClinicalSpecialtySector> findByDescriptionAndSectorId(String description, Integer sectorId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT css FROM  ClinicalSpecialtySector css "
			+ " JOIN Sector s ON css.sectorId = s.id "
			+ " WHERE s.sectorTypeId = :sectorTypeId AND s.institutionId = :institutionId")
	List<ClinicalSpecialtySector> findAllBySectorTypeAndInstitution(@Param("sectorTypeId") Short sectorTypeId,
																	@Param("institutionId") Integer institutionId);

	@Query("SELECT css.description FROM ClinicalSpecialtySector css WHERE css.id = :id")
	String findDescriptionById(@Param("id") Integer id);

	@Query("SELECT css " +
			"FROM ClinicalSpecialtySector css " +
			"JOIN Triage t on (css.id = t.clinicalSpecialtySectorId) " +
			"WHERE t.emergencyCareEpisodeId = :episodeId "+
			"ORDER BY t.id DESC")
	List<ClinicalSpecialtySector> findAllByEpisodeId(@Param("episodeId") Integer episodeId);
}
