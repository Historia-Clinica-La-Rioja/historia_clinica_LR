package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.Rule;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RuleRepository extends SGXAuditableEntityJPARepository<Rule, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT CASE WHEN COUNT (r)>0 THEN true ELse false END " +
			"FROM Rule r " +
			"WHERE r.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"AND r.level = :level " +
			"AND r.deleteable.deleted = false")
	boolean existsByClinicalSpecialtyIdAndLevel(@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId, @Param("level") Short level);

	@Transactional(readOnly = true)
	@Query("SELECT CASE WHEN COUNT (r)>0 THEN true ELse false END " +
			"FROM Rule r " +
			"WHERE r.snomedId = :snomedId " +
			"AND r.level = :level " +
			"AND r.deleteable.deleted = false")
	boolean existsBySnomedIdAndLevel(@Param("snomedId") Integer snomedId, @Param("level") Short level);

	@Transactional(readOnly = true)
	@Query("SELECT r " +
			"FROM Rule r " +
			"WHERE (r.clinicalSpecialtyId = :clinicalSpecialtyId " +
			"OR r.snomedId = :snomedId) " +
			"AND r.deleteable.deleted = FALSE ")
	List<Rule> findByClinicalSpecialtyIdOrSnomedId(@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId, @Param("snomedId") Integer snomedId);

}
