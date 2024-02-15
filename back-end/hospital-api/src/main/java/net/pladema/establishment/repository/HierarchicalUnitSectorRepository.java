package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitSector;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HierarchicalUnitSectorRepository extends SGXAuditableEntityJPARepository<HierarchicalUnitSector, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT case when count(hus) > 0 then TRUE else FALSE END " +
			"FROM HierarchicalUnitSector hus " +
			"WHERE hus.hierarchicalUnitId = :hierarchicalUnitId AND hus.sectorId = :sectorId " +
			"AND hus.deleteable.deleted = false")
	boolean existsByHierarchicalUnitAndSector(@Param("hierarchicalUnitId") Integer hierarchicalUnitId, @Param("sectorId")Integer sectorId);

	@Transactional(readOnly = true)
	@Query("SELECT hus.hierarchicalUnitId " +
			"FROM HierarchicalUnitSector hus " +
			"WHERE hus.sectorId = :sectorId " +
			"AND hus.deleteable.deleted = false")
	List<Integer> getHierarchicalUnitsBySectorId(@Param("sectorId") Integer sectorId);

	@Transactional(readOnly = true)
	@Query("SELECT case when (hc.institutionId = s.institutionId) then TRUE else FALSE end " +
			"FROM HierarchicalUnit hc, Sector s " +
			"WHERE hc.id = :hierarchicalUnitId AND s.id = :sectorId")
	boolean belongToSameInstitution (@Param("hierarchicalUnitId") Integer hierarchicalUnitId, @Param("sectorId") Integer sectorId);

}
