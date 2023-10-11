package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.HistoricInchargeNurseBed;

import org.springframework.stereotype.Repository;

@Repository
public interface HistoricInchargeNurseBedRepository extends SGXAuditableEntityJPARepository<HistoricInchargeNurseBed, Integer> {
}
