package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.Rule;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

	@Transactional(readOnly = true)
	@Query("SELECT r " +
			"FROM Rule r " +
			"JOIN InstitutionalGroupRule igr ON (igr.ruleId = r.id) " +
			"JOIN InstitutionalGroupInstitution igi ON (igr.institutionalGroupId = igi.institutionalGroupId) " +
			"WHERE r.clinicalSpecialtyId IN :clinicalSpecialtyIds " +
			"AND igi.institutionId = :institutionId " +
			"AND igr.regulated IS TRUE " +
			"AND r.deleteable.deleted IS FALSE " +
			"AND igr.deleteable.deleted IS FALSE " +
			"AND igi.deleteable.deleted IS FALSE" )
	List<Rule> findRegulatedRuleByClinicalSpecialtyIdInInstitution(@Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																   @Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT r " +
			"FROM Rule r " +
			"JOIN InstitutionalGroupRule igr ON (igr.ruleId = r.id) " +
			"JOIN InstitutionalGroupInstitution igi ON (igr.institutionalGroupId = igi.institutionalGroupId) " +
			"WHERE r.snomedId = :snomedId " +
			"AND (igi.institutionId = :institutionId) " +
			"AND igr.regulated IS TRUE " +
			"AND r.deleteable.deleted IS FALSE " +
			"AND igr.deleteable.deleted IS FALSE " +
			"AND igi.deleteable.deleted IS FALSE" )
	Optional<Rule> findRegulatedRuleBySnomedIdInInstitution(@Param("snomedId") Integer snomedId,
															@Param("institutionId") Integer institutionId);

}
