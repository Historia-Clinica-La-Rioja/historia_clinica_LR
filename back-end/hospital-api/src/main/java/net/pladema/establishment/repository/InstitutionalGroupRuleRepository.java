package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.domain.InstitutionalGroupRuleVo;
import net.pladema.establishment.repository.entity.InstitutionalGroupRule;

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

}
