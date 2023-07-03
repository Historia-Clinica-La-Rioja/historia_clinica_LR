package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitSector;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HierarchicalUnitSectorRepository extends SGXAuditableEntityJPARepository<HierarchicalUnitSector, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT case when count(hus) > 0 then TRUE else FALSE END " +
			"FROM HierarchicalUnitSector hus " +
			"WHERE hus.hierarchicalUnitId = :hierarchicalUnitId AND hus.sectorId = :sectorId " +
			"AND hus.deleteable.deleted = false")
	public boolean existsByHierarchicalUnitAndSector(@Param("hierarchicalUnitId") Integer hierarchicalUnitId, @Param("sectorId")Integer sectorId);
}
