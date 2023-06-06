package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HierarchicalUnit;
import net.pladema.establishment.repository.entity.HierarchicalUnitRelationship;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HierarchicalUnitRelationshipRepository extends SGXAuditableEntityJPARepository<HierarchicalUnitRelationship, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT CASE WHEN COUNT(hur) > 0 THEN true ELSE false END " +
			"FROM HierarchicalUnitRelationship hur " +
			"WHERE (hur.hierarchicalUnitParentId = :hierarchicalUnitParentId " +
			"AND hur.hierarchicalUnitChildId = :hierarchicalUnitChildId) " +
			"OR (hur.hierarchicalUnitParentId = :hierarchicalUnitChildId " +
			"AND hur.hierarchicalUnitChildId = :hierarchicalUnitParentId)")
	boolean existsRelationship(@Param("hierarchicalUnitChildId") Integer hierarchicalUnitChildId,
							   @Param("hierarchicalUnitParentId") Integer hierarchicalUnitParentId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT CASE WHEN COUNT(hur) > 0 THEN true ELSE false END " +
			"FROM HierarchicalUnitRelationship hur " +
			"WHERE hur.hierarchicalUnitParentId = :hierarchicalUnitId")
	boolean existsParentRelationship(@Param("hierarchicalUnitId") Integer hierarchicalUnitId);
}
