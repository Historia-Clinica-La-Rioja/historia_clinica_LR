package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.domain.InstitutionalGroupRuleVo;
import net.pladema.establishment.repository.entity.InstitutionalGroupRule;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionalGroupRuleRepository extends SGXAuditableEntityJPARepository<InstitutionalGroupRule, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.establishment.repository.domain.InstitutionalGroupRuleVo(igr.id, r.id, igr.institutionalGroupId, cs.name, s.pt, r.level, igr.regulated, igr.comment) " +
			"FROM InstitutionalGroupRule igr " +
			"JOIN Rule r ON (r.id = igr.ruleId) " +
			"LEFT JOIN ClinicalSpecialty cs ON (cs.id = r.clinicalSpecialtyId) " +
			"LEFT JOIN Snomed s ON (r.snomedId = s.id) " +
			"WHERE igr.institutionalGroupId = :institutionalGroupId " +
			"AND igr.deleteable.deleted = FALSE")
	List<InstitutionalGroupRuleVo> findAllByInstitutionalGroupId (@Param("institutionalGroupId") Integer institutionalGroupId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.establishment.repository.domain.InstitutionalGroupRuleVo(igr.id, r.id, igr.institutionalGroupId, cs.name, s.pt, r.level, igr.regulated, igr.comment) " +
			"FROM InstitutionalGroupRule igr " +
			"JOIN Rule r ON (r.id = igr.ruleId) " +
			"LEFT JOIN ClinicalSpecialty cs ON (cs.id = r.clinicalSpecialtyId) " +
			"LEFT JOIN Snomed s ON (r.snomedId = s.id) " +
			"WHERE igr.id = :id " +
			"AND igr.deleteable.deleted = FALSE")
	Optional<InstitutionalGroupRuleVo> findVoById (@Param("id") Integer id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE InstitutionalGroupRule igr " +
			"SET igr.deleteable.deleted = true " +
			", igr.deleteable.deletedOn = CURRENT_TIMESTAMP " +
			", igr.deleteable.deletedBy = ?#{ principal.userId } " +
			"WHERE igr.ruleId IN :ruleIds" )
	void deleteByRuleIds(@Param("ruleIds") List<Integer> ruleIds);

	@Transactional
	@Modifying
	@Query(value = "UPDATE InstitutionalGroupRule igr " +
			"SET igr.deleteable.deleted = true " +
			", igr.deleteable.deletedOn = CURRENT_TIMESTAMP " +
			", igr.deleteable.deletedBy = ?#{ principal.userId } " +
			"WHERE igr.institutionalGroupId = :institutionalGroupId")
	void deleteByInstitutionalGroupId(@Param("institutionalGroupId") Integer institutionalGroupId);

	@Transactional(readOnly = true)
	@Query("SELECT case when count(igr) > 0 then TRUE else FALSE END " +
			"FROM InstitutionalGroupRule igr " +
			"WHERE igr.institutionalGroupId = :institutionalGroupId " +
			"AND igr.ruleId IN :ruleIds " +
			"AND igr.deleteable.deleted = FALSE")
	boolean existsByRuleIdsAndInstitutionalGroupId(@Param("ruleIds") List<Integer> ruleIds, @Param("institutionalGroupId") Integer institutionalGroupId);

}
