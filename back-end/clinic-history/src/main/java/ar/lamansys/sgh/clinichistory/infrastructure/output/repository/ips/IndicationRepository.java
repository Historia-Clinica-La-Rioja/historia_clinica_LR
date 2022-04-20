package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Indication;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

@Repository
public interface IndicationRepository extends SGXAuditableEntityJPARepository<Indication, Integer> {

}
