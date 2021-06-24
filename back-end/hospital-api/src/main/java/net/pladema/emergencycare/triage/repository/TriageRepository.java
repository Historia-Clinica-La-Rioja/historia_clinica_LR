package net.pladema.emergencycare.triage.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.emergencycare.triage.repository.entity.Triage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriageRepository extends SGXAuditableEntityJPARepository<Triage, Integer>, TriageRepositoryCustom {
}