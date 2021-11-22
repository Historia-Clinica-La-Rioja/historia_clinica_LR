package net.pladema.emergencycare.triage.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.triage.repository.entity.Triage;

public interface TriageRepository extends SGXAuditableEntityJPARepository<Triage, Integer>, TriageRepositoryCustom {
}