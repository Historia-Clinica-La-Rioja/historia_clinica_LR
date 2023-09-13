package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.Rule;

public interface RuleRepository extends SGXAuditableEntityJPARepository<Rule, Integer> {
}
