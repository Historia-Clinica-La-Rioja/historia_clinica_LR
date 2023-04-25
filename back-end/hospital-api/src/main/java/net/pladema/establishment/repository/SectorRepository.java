package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.Sector;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectorRepository extends SGXAuditableEntityJPARepository<Sector, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT s "+
            "FROM Sector AS s " +
            "WHERE s.institutionId = :institutionId " +
			"AND deleted IS FALSE ")
    List<Sector> getSectorsByInstitution(@Param("institutionId") Integer institutionId);
    @Transactional(readOnly = true)
    @Query("SELECT s "+
			"FROM Sector AS s " +
			"WHERE s.institutionId = :institutionId " +
			"AND s.sectorTypeId = :sectorTypeId " +
			"AND deleted IS FALSE")
    List<Sector> getSectorsOfTypeByInstitution(@Param("institutionId") Integer institutionId, @Param("sectorTypeId") Short sectorTypeId);

    @Transactional(readOnly = true)
    @Query("SELECT s.institutionId "+
            "FROM Sector AS s " +
            "WHERE s.id = :id " +
			"AND deleted IS FALSE")
    Integer getInstitutionId(@Param("id")  Integer id);

    @Transactional(readOnly = true)
    @Query("SELECT s.id "+
            "FROM Sector AS s " +
			"WHERE deleted IS FALSE")
    List<Integer> getAllIds();

    @Transactional(readOnly = true)
    @Query("SELECT s.id "+
            "FROM Sector AS s " +
            "WHERE s.institutionId IN :institutionsIds " +
			"AND deleted IS FALSE")
    List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);

	@Transactional(readOnly = true)
	@Query("SELECT s " +
			"FROM Sector s " +
			"WHERE s.institutionId = :institutionId " +
			"AND s.description = :description " +
			"AND deleted IS FALSE")
	Optional<Sector> findByInstitutionIdAndDescription(@Param("institutionId") Integer institutionId, @Param("description") String description);

	@Transactional(readOnly = true)
	@Query("SELECT s " +
			"FROM Sector s " +
			"WHERE s.sectorId = :sectorId " +
			"AND deleted IS FALSE")
	List<Sector> getChildSectorsBySectorId(@Param("sectorId") Integer sectorId);

}
