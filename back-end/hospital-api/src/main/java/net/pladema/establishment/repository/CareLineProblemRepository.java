package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.establishment.repository.entity.CareLineProblem;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
