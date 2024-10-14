package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.domain.EmergencyCareAttentionPlaceBo;
import net.pladema.establishment.repository.domain.SectorOfTypeVo;
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
			"AND s.deleteable.deleted IS FALSE ")
    List<Sector> getSectorsByInstitution(@Param("institutionId") Integer institutionId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.establishment.repository.domain.SectorOfTypeVo(s, CASE WHEN COUNT(dso.sectorId) > 0 THEN TRUE ELSE FALSE END) "+
			"FROM Sector AS s " +
			"LEFT JOIN DoctorsOffice AS dso ON s.id = dso.sectorId "+
			"WHERE s.institutionId = :institutionId " +
			"AND s.sectorTypeId = :sectorTypeId " +
			"AND s.deleteable.deleted IS FALSE " +
			"GROUP BY s.id ")
    List<SectorOfTypeVo> getSectorsOfTypeByInstitution(@Param("institutionId") Integer institutionId, @Param("sectorTypeId") Short sectorTypeId);

    @Transactional(readOnly = true)
    @Query("SELECT s.institutionId "+
            "FROM Sector AS s " +
            "WHERE s.id = :id " +
			"AND s.deleteable.deleted IS FALSE")
    Integer getInstitutionId(@Param("id")  Integer id);

    @Transactional(readOnly = true)
    @Query("SELECT s.id "+
            "FROM Sector AS s " +
			"WHERE s.deleteable.deleted IS FALSE")
    List<Integer> getAllIds();

    @Transactional(readOnly = true)
    @Query("SELECT s.id "+
            "FROM Sector AS s " +
            "WHERE s.institutionId IN :institutionsIds " +
			"AND s.deleteable.deleted IS FALSE")
    List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);

	@Transactional(readOnly = true)
	@Query("SELECT s " +
			"FROM Sector s " +
			"WHERE s.institutionId = :institutionId " +
			"AND s.description = :description " +
			"AND s.deleteable.deleted IS FALSE")
	Optional<Sector> findByInstitutionIdAndDescription(@Param("institutionId") Integer institutionId, @Param("description") String description);

	@Transactional(readOnly = true)
	@Query("SELECT s " +
			"FROM Sector s " +
			"WHERE s.sectorId = :sectorId " +
			"AND s.deleteable.deleted IS FALSE")
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

	@Transactional(readOnly = true)
	@Query(" SELECT description " +
			"FROM Sector s " +
			"WHERE s.id = :sectorId")
	String getSectorName(@Param("sectorId") Integer sectorId);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.emergencycare.domain.EmergencyCareAttentionPlaceBo(s.id, s.description, s.sectorOrganizationId, s.sectorId) " +
			"FROM Sector s " +
			"WHERE s.institutionId = :institutionId " +
			"AND s.sectorTypeId = :sectorType " +
			"AND s.deleteable.deleted IS FALSE " +
			"ORDER BY COALESCE(s.sectorId, s.id), CASE WHEN s.sectorId IS NULL THEN 0 ELSE 1 END, s.id")
	List<EmergencyCareAttentionPlaceBo> findAllEmergencyCareSectorByInstitutionOrderByHierarchy(@Param("institutionId") Integer institutionId,
																								@Param("sectorType") Short sectorType);

}