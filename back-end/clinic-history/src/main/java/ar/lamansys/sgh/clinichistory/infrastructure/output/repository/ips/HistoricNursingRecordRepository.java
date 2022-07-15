package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.HistoricNursingRecordStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.HistoricNursingRecordStatusPK;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.stereotype.Repository;

@Repository
public interface HistoricNursingRecordRepository  extends SGXAuditableEntityJPARepository<HistoricNursingRecordStatus, HistoricNursingRecordStatusPK> {
}
