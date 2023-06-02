package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface HierarchicalUnitTypeRepository extends SGXAuditableEntityJPARepository<HierarchicalUnitType, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT hut " +
			"FROM HierarchicalUnitType hut " +
			"WHERE hut.description = :description " +
			"AND hut.deleteable.deleted IS FALSE")
	Optional<HierarchicalUnitType> findByDescription(@Param("description") String description);

	@Transactional(readOnly = true)
	@Query(value = "SELECT exists (SELECT 1 " +
			"FROM hierarchical_unit hu " +
			"JOIN hierarchical_unit_type hut ON (hu.type_id = hut.id) " +
			"WHERE hut.id = :typeId " +
			"AND hu.deleted = false)", nativeQuery = true)
	boolean typeInUse(@Param("typeId") Integer typeId);
}
