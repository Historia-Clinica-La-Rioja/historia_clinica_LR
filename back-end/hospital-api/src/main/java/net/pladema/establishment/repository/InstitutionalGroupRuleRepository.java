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

}
