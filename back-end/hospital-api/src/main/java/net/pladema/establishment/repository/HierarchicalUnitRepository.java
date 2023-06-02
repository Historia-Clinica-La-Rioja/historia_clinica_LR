package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HierarchicalUnit;

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
	@Query(value = "SELECT hu " +
			"FROM HierarchicalUnit  hu " +
			"WHERE hu.alias = :alias " +
			"AND hu.deleteable.deleted IS FALSE")
	Optional<HierarchicalUnit> findByAlias(@Param("alias") String alias);

}
