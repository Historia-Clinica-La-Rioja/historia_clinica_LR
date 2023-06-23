package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitStaff;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HierarchicalUnitStaffRepository extends SGXAuditableEntityJPARepository<HierarchicalUnitStaff, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT hus " +
			"FROM HierarchicalUnitStaff hus " +
			"WHERE hus.hierarchicalUnitId = :hierarchicalUnitId " +
			"AND hus.deleteable.deleted IS FALSE " +
			"ORDER BY hus.responsible DESC")
	List<HierarchicalUnitStaff> findByHierarchicalUnitId(@Param("hierarchicalUnitId") Integer hierarchicalUnitId);
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT hus " +
			"FROM HierarchicalUnitStaff hus " +
			"WHERE hus.hierarchicalUnitId = :hierarchicalUnitId " +
			"AND hus.userId = :userId " +
			"AND hus.deleteable.deleted IS FALSE")
	Optional<HierarchicalUnitStaff> findByHierarchicalUnitIdAndUserId(@Param("hierarchicalUnitId") Integer hierarchicalUnitId,
																		@Param("userId") Integer userId);


}
