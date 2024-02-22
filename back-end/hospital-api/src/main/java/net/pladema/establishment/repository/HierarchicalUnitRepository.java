package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HierarchicalUnit;

import net.pladema.establishment.service.domain.HierarchicalUnitBo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HierarchicalUnitRepository extends SGXAuditableEntityJPARepository<HierarchicalUnit, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT hu.id "+
			"FROM HierarchicalUnit AS hu " +
			"WHERE hu.institutionId IN :institutionsIds " +
			"AND hu.deleteable.deleted IS FALSE ")
	List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.establishment.service.domain.HierarchicalUnitBo(hu.id, hu.alias, hut.id) "+
			"FROM HierarchicalUnit AS hu " +
			"JOIN HierarchicalUnitType hut ON (hu.typeId = hut.id)" +
			"WHERE hu.institutionId = :institutionId " +
			"AND hu.deleteable.deleted IS FALSE ")
	List<HierarchicalUnitBo> getAllByInstitutionId(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN COUNT(hu.id) > 0 THEN TRUE ELSE FALSE END) " +
			"FROM HierarchicalUnit  hu " +
			"WHERE hu.alias = :alias " +
			"AND hu.institutionId = :institutionId " +
			"AND hu.deleteable.deleted IS FALSE")
	boolean existsByAliasAndInstitutionId(@Param("alias") String alias,
										@Param("institutionId") Integer institutionId);

	@Transactional
	@Modifying
	@Query("UPDATE HierarchicalUnit as hu " +
			"SET hu.hierarchicalUnitIdToReport = null, " +
			"hu.updateable.updatedOn = CURRENT_TIMESTAMP, " +
			"hu.updateable.updatedBy = ?#{ principal.userId } " +
			"WHERE hu.id =:id")
	void deleteHierarchicalUnitIdToReport (@Param("id") Integer id);

	@Transactional
	@Modifying
	@Query("UPDATE HierarchicalUnit as hu " +
			"SET hu.hierarchicalUnitIdToReport = :hierarchicalUnitIdToReport, " +
			"hu.updateable.updatedOn = CURRENT_TIMESTAMP, " +
			"hu.updateable.updatedBy = ?#{ principal.userId } " +
			"WHERE hu.id =:id")
	void setHierarchicalUnitIdToReport (@Param("id") Integer id, @Param("hierarchicalUnitIdToReport") Integer hierarchicalUnitIdToReport);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.establishment.service.domain.HierarchicalUnitBo(hu.id, hu.alias) "+
			"FROM HierarchicalUnit AS hu " +
			"JOIN HierarchicalUnitStaff hus ON (hu.id = hus.hierarchicalUnitId) " +
			"WHERE hu.institutionId = :institutionId " +
			"AND hus.userId = :userId " +
			"AND hus.deleteable.deleted IS FALSE " +
			"ORDER BY hu.alias")
	List<HierarchicalUnitBo> getAllByUserIdAndInstitutionId(@Param("userId") Integer userId,
															@Param("institutionId") Integer institutionId);


	@Transactional(readOnly = true)
	@Query("SELECT hu2.id "+
			"FROM HierarchicalUnit hu " +
			"JOIN HierarchicalUnit hu2 ON (hu.id = hu2.hierarchicalUnitIdToReport) " +
			"WHERE hu.id = :hierarchicalUnitId")
	List<Integer> getAllDescendantIdsByHierarchicalUnitId(@Param("hierarchicalUnitId") Integer hierarchicalUnitId);
	
}
