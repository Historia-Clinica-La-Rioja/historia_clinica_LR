package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.Rule;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RuleRepository extends SGXAuditableEntityJPARepository<Rule, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT CASE WHEN COUNT (r)>0 THEN true ELse false END " +
			"FROM Rule r " +
			"WHERE r.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.deleteable.deleted = false")
	boolean existsByClinicalSpecialtyId (@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

	@Transactional(readOnly = true)
	@Query("SELECT CASE WHEN COUNT (r)>0 THEN true ELse false END " +
			"FROM Rule r " +
			"WHERE r.snomedId = :snomedId " +
			"AND r.deleteable.deleted = false")
	boolean existsBySnomedId(@Param("snomedId") Integer snomedId);

	@Transactional(readOnly = true)
	@Query("SELECT r " +
			"FROM Rule r " +
			"WHERE r.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"OR r.snomedId = :snomedId ")
	Optional<Rule> findByClinicalSpecialtyIdOrSnomedId(@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId, @Param("snomedId") Integer snomedId);

}
