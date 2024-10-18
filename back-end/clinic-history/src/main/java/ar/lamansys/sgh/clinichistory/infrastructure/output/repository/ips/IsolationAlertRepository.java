package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Indication;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation.IsolationAlert;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.stereotype.Repository;

@Repository
public interface IsolationAlertRepository extends SGXAuditableEntityJPARepository<IsolationAlert, Integer> {
}
