package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.domain.isolation.FetchPatientIsolationAlertBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Indication;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation.IsolationAlert;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IsolationAlertRepository extends SGXAuditableEntityJPARepository<IsolationAlert, Integer> {

}
