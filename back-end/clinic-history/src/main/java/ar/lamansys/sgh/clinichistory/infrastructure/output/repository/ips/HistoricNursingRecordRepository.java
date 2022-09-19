package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.HistoricNursingRecordStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.HistoricNursingRecordStatusPK;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HistoricNursingRecordRepository  extends SGXAuditableEntityJPARepository<HistoricNursingRecordStatus, HistoricNursingRecordStatusPK> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT hnrs "
				+ "FROM HistoricNursingRecordStatus hnrs "
				+ "WHERE hnrs.pk.nursingRecordId = :nursingRecordId "
			 	+ "ORDER BY hnrs.pk.changedStatusDate DESC ")
	List<HistoricNursingRecordStatus> getAllByNursingRecordId (@Param("nursingRecordId") Integer nursingRecordId);

}
