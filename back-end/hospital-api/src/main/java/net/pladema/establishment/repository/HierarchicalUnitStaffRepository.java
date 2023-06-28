package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitStaff;

import net.pladema.establishment.service.domain.HierarchicalUnitStaffBo;

import org.springframework.data.jpa.repository.Modifying;
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

	@Transactional
	@Query(value = "SELECT hus " +
			"FROM HierarchicalUnitStaff hus " +
			"WHERE hus.userId = :userId " +
			"AND hus.deleteable.deleted IS FALSE")
	List<HierarchicalUnitStaff> findByUserId(@Param("userId") Integer userId);

	@Transactional
	@Query(value = "SELECT hus " +
			"FROM HierarchicalUnitStaff hus " +
			"JOIN HierarchicalUnit hu ON hus.hierarchicalUnitId = hu.id " +
			"WHERE hus.userId = :userId " +
			"AND hu.institutionId = :institutionId " +
			"AND hu.deleteable.deleted IS FALSE " +
			"AND hus.deleteable.deleted IS FALSE")
	List<HierarchicalUnitStaff> findByUserIdAndInstitutionId(@Param("userId") Integer userId,
															 @Param("institutionId") Integer institutionId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE HierarchicalUnitStaff hus "
			+ "SET hus.deleteable.deleted = true "
			+ ", hus.deleteable.deletedOn = CURRENT_TIMESTAMP "
			+ ", hus.deleteable.deletedBy = ?#{ principal.userId } "
			+ "WHERE hus.userId = :userId" )
	void deleteHierarchicalUnitStaffByUserId(@Param("userId") Integer userId);

}
