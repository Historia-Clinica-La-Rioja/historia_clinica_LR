package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HierarchicalUnit;
import net.pladema.establishment.repository.entity.HierarchicalUnitRelationship;

import org.springframework.data.jpa.repository.Modifying;
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

	@Transactional(readOnly = true)
	@Query(value = "SELECT hu " +
			"FROM HierarchicalUnitRelationship hur " +
			"JOIN HierarchicalUnit hu ON (hur.hierarchicalUnitParentId = hu.id) " +
			"WHERE hur.hierarchicalUnitChildId = :hierarchicalUnitId " +
			"AND hur.deleteable.deleted = false " +
			"ORDER BY hu.alias ASC")
	List<HierarchicalUnit> findParentsIdsByHierarchicalUnitChildId(@Param("hierarchicalUnitId") Integer hierarchicalUnitId);

	@Modifying
	@Transactional(readOnly = true)
	@Query(value = "UPDATE HierarchicalUnitRelationship hur "
			+ "SET hur.deleteable.deleted = true "
			+ ", hur.deleteable.deletedOn = CURRENT_TIMESTAMP "
			+ ", hur.deleteable.deletedBy = ?#{ principal.userId } "
			+ "WHERE hur.hierarchicalUnitChildId = :hierarchicalUnitId")
	void deleteByHierarchicalUnitId(@Param("hierarchicalUnitId") Integer hierarchicalUnitId);
}
