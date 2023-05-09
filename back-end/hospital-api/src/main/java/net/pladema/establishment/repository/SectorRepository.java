package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.Sector;

import net.pladema.establishment.service.domain.AttentionPlacesQuantityBo;

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

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.establishment.service.domain.AttentionPlacesQuantityBo(COUNT(DISTINCT CASE WHEN s.deleteable.deleted = false THEN 1 END), COUNT(DISTINCT CASE WHEN do.deleteable.deleted = false THEN 1 END), COUNT(DISTINCT b.id)) " +
			"FROM Sector AS se " +
			"LEFT JOIN DoctorsOffice AS do ON do.sectorId = se.id " +
			"LEFT JOIN Shockroom AS s ON s.sectorId = se.id " +
			"LEFT JOIN Room AS r ON r.sectorId = se.id " +
			"LEFT JOIN Bed AS b ON b.roomId = r.id " +
			"WHERE se.institutionId = :institutionId " +
			"AND se.sectorTypeId = :sectorTypeId ")
	AttentionPlacesQuantityBo quantityAttentionPlacesBySectorType(@Param("institutionId") Integer institutionId,
															@Param("sectorTypeId") Short sectorTypeId);

}