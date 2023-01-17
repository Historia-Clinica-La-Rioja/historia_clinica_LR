package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DiagnosticReportRepository extends SGXAuditableEntityJPARepository<DiagnosticReport, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE DiagnosticReport AS dr " +
			"SET dr.patientId = :newPatientId " +
			"WHERE dr.id IN :drIds")
	void updatePatient(@Param("drIds") List<Integer> drIds,@Param("newPatientId") Integer newPatientId);
}
