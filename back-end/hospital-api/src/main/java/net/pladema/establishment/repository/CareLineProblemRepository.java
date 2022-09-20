package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.establishment.repository.entity.CareLineProblem;

import org.springframework.stereotype.Repository;

@Repository
public interface CareLineProblemRepository extends SGXAuditableEntityJPARepository<CareLineProblem, Integer> {
}
