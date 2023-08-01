package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.establishment.repository.entity.CareLineProblem;

import net.pladema.establishment.service.domain.CareLineProblemBo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareLineProblemRepository extends SGXAuditableEntityJPARepository<CareLineProblem, Integer> {

	@Query("SELECT clp " +
			"FROM CareLineProblem as clp " +
			"WHERE clp.careLineId = :careLineId " +
			"AND (clp.deleteable.deleted is null or clp.deleteable.deleted = false)")
	List<CareLineProblem> findByCareLineId(@Param("careLineId") Integer careLineId);

	@Query("SELECT clp " +
			"FROM CareLineProblem clp " +
			"WHERE clp.careLineId = :careLineId " +
			"AND clp.snomedId = :snomedId ")
	Optional<CareLineProblem> findByCareLineIdAndSnomedId(@Param("careLineId") Integer careLineId, @Param("snomedId") Integer snomedId);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.establishment.service.domain.CareLineProblemBo(clp.careLineId, s.id, s.sctid, s.pt) " +
			"FROM CareLineProblem clp " +
			"JOIN Snomed s ON (clp.snomedId = s.id) " +
			"WHERE clp.careLineId IN (:careLineIds) " +
			"AND clp.deleteable.deleted IS FALSE")
	List<CareLineProblemBo> getAllByCareLineIds(@Param("careLineIds") List<Integer> careLineIds);
}
